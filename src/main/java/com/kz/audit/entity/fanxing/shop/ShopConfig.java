package com.kz.audit.entity.fanxing.shop;

import com.kz.audit.entity.db.Entity;
import lombok.Data;

import java.io.Serializable;

/**
 * 设置配置表
 *
 * @author kz
 * @email jmyinjg@163.com
 * @date 2021-07-07 20:15:05
 */
@Entity.Cache(region = "ShopConfig")
@Data
public class ShopConfig extends Entity implements Serializable {

    public static final ShopConfig ME = new ShopConfig();

    @Override
    public String tableName() {
        return "shop_config";
    }

    @Override
    public String databaseName() {
        return "fanxing";
    }

    @Override
    protected String schemaName() {
        return "fanxing_shop";
    }

    /**
     * 商户号
     */
    private String tenant_code;

    /**
     * 门店ID
     */
    private String shop_id;

    /**
     * 配置项关键字
     */
    private String config_key;

    /**
     * 配置值json
     */
    private String config;

    /**
     * 配置描述
     */
    private String config_desc;

    /**
     * 是否启用，0=禁用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 版本号
     */
    private Integer version;

}
