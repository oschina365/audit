package com.kz.audit.entity.db;

import lombok.Data;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据库对象的基类
 *
 * @author winterlau
 */
@Data
public abstract class Entity implements Serializable {

    public static final String CACHE_ONE_HOUR = "one_hour";
    public static final String CACHE_ONE_MIN = "one_min";
    public static final String CACHE_FIVE_MIN = "five_min";

    private Date create_time;


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Cache {
        String region();

        boolean cacheNull() default true;
    }

    protected final static transient String OBJ_COUNT_CACHE_KEY = "#";
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * 返回对象对应的缓存区域名
     *
     * @return
     */
    public String CacheRegion() {
        return this.getClass().getSimpleName();
    }

    /**
     * 对象是否存在
     *
     * @param e
     * @return
     */
    public boolean notNullObject(Entity e) {
        if (e != null && e.getId() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 分页列出所有对象
     *
     * @param page
     * @param size
     * @return
     */
    public List<? extends Entity> list(int page, int size) {
        String sql = "SELECT * FROM " + rawTableName() + " ORDER BY id DESC";
        return DbQuery.get(databaseName()).query_slice(getClass(), sql, page, size);
    }

    /**
     * 分页列出所有对象
     *
     * @param page
     * @param size
     * @return
     */
    public List<Long> listIds(int page, int size) {
        String sql = "SELECT * FROM " + rawTableName() + " ORDER BY id DESC";
        return DbQuery.get(databaseName()).query_slice(Long.class, sql, page, size);
    }

    /**
     * 查询所有对象
     *
     * @return
     */
    public List<? extends Entity> list(boolean fromCache) {
        String sql = "SELECT * FROM " + rawTableName();
        if (fromCache) {
            return DbQuery.get(databaseName()).query_cache(getClass(), cacheNullObject(), cacheRegion(), "all", sql);
        }
        return DbQuery.get(databaseName()).query(getClass(), sql);
    }

    public List<? extends Entity> filter(String filter, int page, int size) {
        String sql = "SELECT * FROM " + rawTableName() + " WHERE " + filter + " ORDER BY id DESC";
        return DbQuery.get(databaseName()).query_slice(getClass(), sql, page, size);
    }

    /**
     * 统计此对象的总记录数
     *
     * @return
     */
    public int totalCount(String filter) {
        return DbQuery.get(databaseName()).stat("SELECT COUNT(*) FROM " + rawTableName() + " WHERE " + filter);
    }

    public static void evictCache(String cache, String key) {
        CacheMgr.evict(cache, key);
    }


    /**
     * 返回默认的对象对应的表名
     *
     * @return
     */
    public final String rawTableName() {
        String schemaName = schemaName();
        return (schemaName != null) ? "`" + schemaName + "`." + tableName() : tableName();
    }

    protected String tableName() {
        return Inflector.getInstance().tableize(getClass());
    }

    protected String schemaName() {
        return null;
    }

    protected String databaseName() {
        return null;
    }

    /**
     * 插入对象到数据库表中
     *
     * @return
     */
    public long save(boolean evict) {
        if (getId() > 0) {
            _InsertObject(this);
        } else {
            setId(_InsertObject(this));
        }

        if (evict && this.cachedByID()) {
            CacheMgr.evict(cacheRegion(), OBJ_COUNT_CACHE_KEY);
            if (cacheNullObject()) {
                CacheMgr.evict(cacheRegion(), String.valueOf(getId()));
            }
        }
        return getId();
    }

    /**
     * 根据id主键删除对象
     *
     * @return
     */
    public boolean delete() {
        boolean dr = evict(DbQuery.get(databaseName()).update("DELETE FROM " + rawTableName() + " WHERE id=?", getId()) == 1);
        if (dr && cachedByID()) {
            CacheMgr.evict(cacheRegion(), OBJ_COUNT_CACHE_KEY);
        }
        return dr;
    }

    /**
     * 根据条件决定是否清除对象缓存
     *
     * @param er
     * @return
     */
    public boolean evict(boolean er) {
        if (er && cachedByID()) {
            CacheMgr.evict(cacheRegion(), String.valueOf(getId()));
        }
        return er;
    }

    /**
     * 清除指定主键的对象缓存
     *
     * @param obj_id
     */
    public void evict(long obj_id) {
        CacheMgr.evict(cacheRegion(), String.valueOf(obj_id));
    }

    /**
     * 根据主键读取对象详细资料，根据预设方法自动判别是否需要缓存
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Entity> T get(long id) {
        if (id <= 0) {
            return null;
        }

        String sql = "SELECT * FROM " + rawTableName() + " WHERE id = ?";
        Cache cache = getClass().getAnnotation(Cache.class);
        return (cache != null) ?
                (T) DbQuery.get(databaseName()).read_cache(getClass(), cache.cacheNull(), cache.region(), String.valueOf(id), sql, id) :
                (T) DbQuery.get(databaseName()).read(getClass(), sql, id);
    }

    /**
     * 根据主键读取对象详细资料，可以排除某些字段，不缓存
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Entity> T get(int id, String[] exclusiveFields) {
        if (id <= 0) {
            return null;
        }
        Map<String, Object> pojo_bean = this.listInsertableFields();
        pojo_bean.put("id", getId());
        StringBuilder sql = new StringBuilder("SELECT ");
        int i = 0;
        for (String field : pojo_bean.keySet()) {
            if (ArrayUtils.contains(exclusiveFields, field)) {
                continue;
            }
            if (i > 0) {
                sql.append(',');
            }
            sql.append("\"");
            sql.append(field);
            sql.append("\"");
            i++;
        }
        sql.append(" FROM ");
        sql.append(rawTableName());
        sql.append(" WHERE id = ?");
        return (T) DbQuery.get(databaseName()).read(getClass(), sql.toString(), id);
    }

    public List<? extends Entity> get(List<Long> ids) {
        if (ids.isEmpty()) {
            return null;
        }
        StringBuilder sql = new StringBuilder("SELECT * FROM " + rawTableName() + " WHERE id IN (");
        for (int i = 1; i <= ids.size(); i++) {
            sql.append('?');
            if (i < ids.size()) {
                sql.append(',');
            }
        }
        sql.append(')');
        List<? extends Entity> beans = DbQuery.get(databaseName()).query(getClass(), sql.toString(), ids.toArray(new Object[ids.size()]));
        if (cachedByID()) {
            for (Object bean : beans) {
                CacheMgr.set(cacheRegion(), String.valueOf(((Entity) bean).getId()), bean);
            }
        }
        return beans;
    }

    /**
     * 统计此对象的总记录数
     *
     * @return
     */
    public int totalCount() {
        if (this.cachedByID()) {
            return DbQuery.get(databaseName()).stat_cache(cacheRegion(), OBJ_COUNT_CACHE_KEY, "SELECT COUNT(*) FROM " + rawTableName());
        }

        return DbQuery.get(databaseName()).stat("SELECT COUNT(*) FROM " + rawTableName());
    }


    /**
     * 批量加载项目
     *
     * @param p_pids
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    public List loadList(List<? extends Number> p_pids) {
        if (p_pids.isEmpty()) {
            return null;
        }
        List<Long> pids_l = p_pids.stream().map(Number::longValue).collect(Collectors.toList());
        List<Long> pids = new LinkedList<>();
        pids.addAll(pids_l);

        List<Entity> prjs = new ArrayList<Entity>(pids.size()) {
            {
                for (int i = 0; i < pids.size(); i++) {
                    add(null);
                }
            }
        };
        List<Long> no_cache_ids = new ArrayList<>();

        if (!this.cachedByID()) {
            no_cache_ids.addAll(pids_l);
        } else {
            String cache = this.cacheRegion();
            for (int i = 0; i < pids.size(); i++) {
                long pid = pids.get(i);
                Entity obj = (Entity) CacheMgr.get(cache, String.valueOf(pid));

                if (obj != null) {
                    prjs.set(i, obj);
                } else {
                    no_cache_ids.add(pid);
                }
            }
        }

        if (no_cache_ids.isEmpty()) {
            return prjs;
        }

        List<? extends Entity> no_cache_prjs = get(no_cache_ids);
        if (!no_cache_ids.isEmpty()) {
            no_cache_prjs.stream().forEach(s -> {
                prjs.set(pids.indexOf(s.getId()), s);
            });
        }

        return prjs;
    }

    /**
     * 更新某个字段值
     *
     * @param field
     * @param value
     * @return
     */
    public boolean updateField(String field, Object value) {
        String sql = "UPDATE " + rawTableName() + " SET " + field + " = ? WHERE id=?";
        return evict(DbQuery.get(databaseName()).update(sql, value, getId()) == 1);
    }

    /**
     * 更新多个字段值
     *
     * @param map
     * @return
     */
    public boolean updateFields(Map<String, Object> map) {
        if (map != null) {
            Object id = map.remove("id");
            Object[] params = new Object[map.keySet().size()];
            StringBuilder sql = new StringBuilder("update ").append(tableName()).append(" set ");
            int index = 0;
            for (String key : map.keySet()) {
                sql.append("`" + key + "`").append("=?,");
                params[index] = map.get(key);
                index++;
            }
            sql.replace(sql.length() - 1, sql.length(), " where id=");
            sql.append(getId());
            return evict(DbQuery.get(databaseName()).update(sql.toString(), params) == 1);
        }
        return false;
    }

    /**
     * 执行 INSERT ... ON DUPLICATE KEY UPDATE 并返回 LAST_INSERT_ID
     *
     * @param sql
     * @param params
     * @return
     */
    public int executeInsertOrUpdateSQLAndReturnId(String sql, Object... params) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = DbQuery.get(databaseName()).conn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
        }
    }

    /**
     * 插入对象
     *
     * @param obj
     * @return 返回插入对象的主键
     */
    private long _InsertObject(Entity obj) {
        Map<String, Object> pojo_bean = obj.listInsertableFields();
        if (this.getId() > 0) {
            pojo_bean.put("id", this.getId());
        }
        String[] fields = pojo_bean.keySet().stream().toArray(String[]::new);
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(obj.rawTableName());
        //双引号导致  形如 INSERT INTO osc_blogs("origin_url","abstracts","catalog","project", 语法异常，此处修改剔除双引号
        //修改为反单引号
        //sql.append("(\"");
        sql.append("(`");
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sql.append("`,`");
            }
            sql.append(fields[i]);
        }
        //sql.append("\") VALUES(");
        sql.append("`) VALUES(");
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sql.append(',');
            }
            sql.append('?');
        }
        sql.append(')');
        try (PreparedStatement ps = DbQuery.get(databaseName()).conn().prepareStatement(sql.toString(),
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < fields.length; i++) {
                ps.setObject(i + 1, pojo_bean.get(fields[i]));
            }

            ps.executeUpdate();
            if (getId() > 0) {
                return getId();
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * 列出要插入到数据库的字段集合，子类可以覆盖此方法
     *
     * @return
     */
    protected Map<String, Object> listInsertableFields() {
        Map<String, Object> props = new HashMap<>();
        try {
            PropertyDescriptor[] fields = Introspector.getBeanInfo(getClass()).getPropertyDescriptors();
            for (PropertyDescriptor field : fields) {
                if ("class".equals(field.getName())) {
                    continue;
                }
                if (getId() == 0 && "id".equals(field.getName())) {
                    continue;
                }
                if ("vip_text".equalsIgnoreCase(field.getName())) {
                    continue;
                }
                if ("sdf_insert_date".equalsIgnoreCase(field.getName())) {
                    continue;
                }
                if ("sdf_last_date".equalsIgnoreCase(field.getName())) {
                    continue;
                }
                Object fv = field.getReadMethod().invoke(this);
                if (fv == null /*|| ((fv instanceof Number) && ((Number) fv).intValue() == 0)*/) {
                    continue;
                }
                props.put(field.getName(), fv);
            }

            return props;

        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("ListInsertableFields Failed", e);
        }
    }

    public long user() {
        try {
            PropertyDescriptor[] fields = Introspector.getBeanInfo(getClass()).getPropertyDescriptors();
            for (PropertyDescriptor field : fields) {
                if ("user".equalsIgnoreCase(field.getName())) {
                    Object fv = field.getReadMethod().invoke(this);
                    return ((long) fv);
                }
            }
        } catch (Exception e) {
        }
        return 0L;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        // 不同的子类尽管ID是相同也是不相等的
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        Entity wb = (Entity) obj;
        return wb.getId() == getId();
    }

    /**
     * 返回对象对应的缓存区域名
     *
     * @return
     */
    public String cacheRegion() {
        Cache cache = this.getClass().getAnnotation(Cache.class);
        return (cache != null) ? cache.region() : null;
    }

    /**
     * 是否根据ID缓存对象，此方法对Get(long id)有效
     *
     * @return
     */
    private boolean cachedByID() {
        Cache cache = this.getClass().getAnnotation(Cache.class);
        return cache != null;
    }

    private boolean cacheNullObject() {
        Cache cache = this.getClass().getAnnotation(Cache.class);
        return cache != null && cache.cacheNull();
    }

    /**
     * 更新对象
     *
     * @return
     */
    public boolean doUpdate(boolean evict) {
        Map<String, Object> map = listInsertableFields();
        Object id = map.remove("id");
        Set<Map.Entry<String, Object>> entrys = map.entrySet();
        Object[] params = new Object[entrys.size()];
        StringBuilder sql = new StringBuilder("update ").append(tableName()).append(" set ");
        int index = 0;
        for (Map.Entry<String, Object> entry : entrys) {
            sql.append("`" + entry.getKey() + "`").append("=?,");
            params[index] = entry.getValue();
            index++;
        }
        sql.replace(sql.length() - 1, sql.length(), " where id=");
        sql.append(id);
        if (evict) {
            CacheMgr.evict(this.CacheRegion(), String.valueOf(this.getId()));
        }
        return DbQuery.get(databaseName()).update(sql.toString(), params) > 0;
    }


    public List<? extends Entity> Filter(String filter, int page, int size, Object... params) {
        String sql = "SELECT * FROM " + tableName();
        if (StringUtils.isNotBlank(filter)) {
            if (filter.toLowerCase().contains("where")) {
                sql += filter;
            } else {
                sql += " WHERE " + filter;
            }
        }
        if (StringUtils.isNotBlank(filter) && !filter.toLowerCase().contains("order by")) {
            sql += " order by id desc";
        }
        return DbQuery.get(databaseName()).query_slice(getClass(), sql, page, size, params);
    }

    /**
     * 根据条件获取id，再根据id获取该对象
     *
     * @param t
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T extends Entity> T getById(T t, String sql, Object... params) {
        if (StringUtils.isBlank(sql)) {
            return null;
        }
        Number n = DbQuery.get(databaseName()).read(Number.class, sql, params);
        if (n == null || n.longValue() <= 0L) {
            return null;
        }
        return t.get(n.longValue());
    }

    /**
     * 根据条件获取id集合，再获取对象
     *
     * @param t
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T extends Entity> List<T> sliceByIds(T t, String sql, int page, int size, Object... params) {
        if (StringUtils.isBlank(sql)) {
            return null;
        }
        List<Number> numbers = DbQuery.get(databaseName()).query_slice(Number.class, sql, page, size, params);
        if (numbers.isEmpty()) {
            return null;
        }
        List<Long> ids = numbers.stream().map(Number::longValue).collect(Collectors.toList());
        return t.loadList(ids);
    }

    /**
     * 根据条件获取id
     *
     * @param t
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T extends Entity> Number getId(T t, String sql, Object... params) {
        if (StringUtils.isBlank(sql)) {
            return null;
        }
        return DbQuery.get(databaseName()).read(Number.class, sql, params);
    }

}
