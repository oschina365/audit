package com.kz.audit.alipay;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 应用添加成员
 *
 * @author kz
 * @create 2021-10-11 14:10
 **/
@Data
public class AlipayOpenAppMembersCreateBizRequest {

    /**
     * 支付宝登录账号
     */
    @JSONField(name = "logon_id")
    private String logon_id;

    /**
     * 为成员添加的角色类型，支持：DEVELOPER-开发者；EXPERIENCER-体验者
     */
    private String role;
}
