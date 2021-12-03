package com.kz.audit.entity;


import com.kz.audit.entity.db.Entity;
import lombok.Data;

import java.io.Serializable;

/**
 * isv授权列表
 *
 * @author kz
 * @since 2021-11-05
 */
@Data
@Entity.Cache(region = "AlipayTokenEntity")
public class AuditAlipayEntity extends Entity implements Serializable {

    public static final AuditAlipayEntity ME = new AuditAlipayEntity();

    @Override
    public String tableName() {
        return "audit_alipay";
    }

    /**
     * 小程序名称
     */
    private String name;

    /**
     * isv授权
     */
    private String token;

    /**
     * token生效状态
     */
    private Integer status;

    /**
     * isv类型， 1=qiying ,2=qiying_coupon
     */
    private Integer type;

    private String create_ip;
}
