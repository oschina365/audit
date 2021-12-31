package com.kz.audit.entity.alipay;

import lombok.Data;

/**
 * @author kz
 * @since 2021-12-18 10:55:59
 */
@Data
public class MiniUperJson {


    private Alipay alipay;

    @Data
    public static class Alipay {
        private String appid;
        private String toolId;
        private String projectPath;
        private String privateKeyPath;
        private Experience experience;
    }

    @Data
    public static class Experience {
        private String url;
        private String method;
        private String contentType;
        private String body;
    }

    public static MiniUperJson build(String appid) {
        MiniUperJson ME = new MiniUperJson();
        Alipay alipay = new Alipay();

        alipay.setAppid(appid);
        alipay.setToolId("ae741091a0eb4ff69c85d44242a489ee");
        alipay.setProjectPath("");
        alipay.setPrivateKeyPath("pkcs8-private-pem");
        Experience ex = new Experience();
        ex.setUrl("");
        ex.setMethod("");
        ex.setContentType("application/json");
        ex.setBody("{\"支付宝小程序体验版\":\"{{qrCodeUrl}}\",\"版本号\":\"{{version}}\",\"版本描述\":\"{{description}}\"}");
        alipay.setExperience(ex);

        ME.setAlipay(alipay);
        return ME;
    }
}
