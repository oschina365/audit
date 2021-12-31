package com.kz.audit.dao.alipay.vo;

import lombok.Data;

/**
 * 支付宝小程序配置-渠道
 *
 * @author jmy
 * @version 1.1
 * @date 2021-08-30 13:04
 */
@Data
public class AlipayAppletConfigTO {

    /**
     * 应用ID
     * ISV模式-企颖商城应用ID
     * 普通小程序-普通小程序APPID
     */
    private String appId;

    /**
     * 小程序APPID
     * ISV模式下，必填，订单同步需要
     * 普通小程序模式，非必填
     */
    private String appletId;

    /**
     * 商家支付宝账户pid
     */
    private String pid;

    /**
     * 应用名称
     */
    private String appName;
    /**
     * 应用二维码
     */
    private String appQRCode;
    /**
     * 应用私钥
     */
    private String privateKey;
    /**
     * 应用公钥
     */
    private String publicKey;
    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;
    /**
     * 授权TOKEN
     */
    private String appAuthToken;
    /**
     * 解密key
     */
    private String aesKey;

}
