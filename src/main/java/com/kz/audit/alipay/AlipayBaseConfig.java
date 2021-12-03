package com.kz.audit.alipay;


import lombok.Data;

/**
 * @author kz
 * @create 2021-08-23 10:19
 **/
@Data
public class AlipayBaseConfig {

    private String appId;

    private String privateKey;

    private String publicKey;

    private String alipayPublicKey;

    private String appAuthToken;

    private String notifyUrl;

    private String appAes;


}
