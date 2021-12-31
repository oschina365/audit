package com.kz.audit.dao.fanxing.shop;

import com.kz.audit.dao.CommonDao;
import com.kz.audit.entity.fanxing.shop.ShopConfig;

import java.util.List;

/**
 * @author kz
 * @since 2021-12-31 11:45:13
 */
public class ShopConfigDAO extends CommonDao<ShopConfig> {

    public static final ShopConfigDAO ME = new ShopConfigDAO();

    @Override
    protected String databaseName() {
        return "fanxing";
    }

    public List<ShopConfig> getByKey(String key) {
        String sql = "select * from " + ShopConfig.ME.rawTableName() + " where config_key=? order by tenant_code asc ";
        return getDbQuery().query(ShopConfig.class, sql, key);
    }

}
