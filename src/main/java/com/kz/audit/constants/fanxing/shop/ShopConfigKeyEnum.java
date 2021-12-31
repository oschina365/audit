package com.kz.audit.constants.fanxing.shop;

import java.util.HashMap;
import java.util.Map;

/**
 * 商城配置KEY
 *
 * @author jmy
 * @version 1.1
 * @date 2021-07-17 11:46
 */
public enum ShopConfigKeyEnum {

    DEFAULT_IMG("default_img", "默认图配置"),
    DIY_STYLE("diy_style", "主题色"),
    DIY_CATEGORY("diy_category", "自定义分类页面样式"),
    DIY_BOTTOM_NAV("diy_bottom_nav", "自定义底部导航栏样式"),
    DIY_MEMBER_INDEX_CONFIG("diy_member_index_config", "自定义会员中心样式"),

    WX_APPLET_CONFIG("wx_applet_config", "微信小程序配置"),
    ALIPAY_APPLET_CONFIG("alipay_applet_config", "支付宝小程序配置"),
    WECHAT_PAY_CONFIG("wechat_pay_config", "微信支付配置"),
    ALIPAY_CONFIG("alipay_config", "支付宝支付配置"),
    WX_APPLET_SHARE_CONFIG("wx_applet_share_config", "小程序分享设置"),
    GOOD_SORT_CONFIG("good_sort_config", "商品排序设置"),
    GOOD_DEFAULT_SEARCH_CONFIG("good_default_search_config", "商品默认搜索"),
    GOOD_HOT_SEARCH_CONFIG("good_hot_search_config", "商品热门搜索"),
    TRADE_CONFIG("trade_config", "交易设置"),
    DELIVERY_WAY_CONFIG("delivery_way_config", "配送方式设置"),
    DELIVERY_MANAGE_CONFIG("delivery_manage_config", "配送管理设置");


    private String key;
    private String desc;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    ShopConfigKeyEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static final Map<String, String> keyMap = new HashMap<>();

    static {
        for (ShopConfigKeyEnum keyEnum : ShopConfigKeyEnum.values()) {
            keyMap.put(keyEnum.key, keyEnum.desc);
        }
    }

}
