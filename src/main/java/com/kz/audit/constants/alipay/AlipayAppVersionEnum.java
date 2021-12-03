package com.kz.audit.constants.alipay;

/**
 * 支付宝小程序的版本状态
 *
 * @author kz
 * @since 2021-11-6 15:27:26
 */
public enum AlipayAppVersionEnum {

    INIT("INIT", "开发中"),
    AUDITING("AUDITING", "审核中"),
    AUDIT_REJECT("AUDIT_REJECT", "审核驳回"),
    WAIT_RELEASE("WAIT_RELEASE", "待上架"),
    GRAY("GRAY", "灰度中"),
    RELEASE("RELEASE", "已上架"),
    OFFLINE("OFFLINE", "已下架"),
    AUDIT_OFFLINE("AUDIT_OFFLINE", "已下架");

    private String key;
    private String desc;

    AlipayAppVersionEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }
}
