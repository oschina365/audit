package com.kz.audit.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.FileItem;
import com.alipay.api.request.AlipayInsScenePetprofilePlatformprofileCheckRequest;
import com.alipay.api.request.AlipayOpenFileUploadRequest;
import com.alipay.api.response.AlipayInsScenePetprofilePlatformprofileCheckResponse;
import com.alipay.api.response.AlipayOpenFileUploadResponse;
import com.kz.audit.alipay.AlipayBase;
import com.kz.audit.alipay.AlipayBaseConfig;
import com.kz.audit.constants.alipay.AlipayProfileConstant;
import com.kz.audit.util.FileUtils;
import com.kz.audit.vo.alipay.profile.AlipayProfileCheckVO;

public class TestProfile {

    public static void main(String[] args) {
        /*String fileUrl = "http://img.oscer.net/alipay/profile/dog/dog1.jpg";
        System.out.println(JSONObject.toJSONString(profileUpload(fileUrl)));*/

        /*AlipayProfileCheckVO vo = new AlipayProfileCheckVO();
        vo.setFileId("A*krfPT7bSbY4AAAAAAAAAAAAADtp1AA");
        vo.setPetType(PetEnum.TYPE.DOG.getCode());
        vo.setPetPhotoType(PetEnum.PHOTO_TYPE.NOSE.getType());
        System.out.println(JSONObject.toJSONString(check(vo)));*/
    }

    public static AlipayOpenFileUploadResponse profileUpload(String fileUrl) {
        AlipayOpenFileUploadRequest request = new AlipayOpenFileUploadRequest();
        request.setBizCode("content_creation");
        FileItem FileContent = new FileItem(fileUrl.substring(fileUrl.lastIndexOf("/")), FileUtils.getFileStream(fileUrl));
        request.setFileContent(FileContent);
        AlipayOpenFileUploadResponse response = null;
        try {
            response = (AlipayOpenFileUploadResponse) getAlipayClient(config()).execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response == null || !response.isSuccess()) {
            System.out.println(String.format("method>>>{%s} error，request param :{%s}", request.getApiMethodName(), fileUrl));
        }
        /**
         * {
         *     "body":"{\"alipay_open_file_upload_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"file_id\":\"A*krfPT7bSbY4AAAAAAAAAAAAADtp1AA\"},\"sign\":\"gDjS4vFuGJODJyhLLKPrG/X6Z9ZTj2e4NQUd7BdjWXFxq9hLOOwIoeOzCJA702MNI9ufP6s87UEk/I+XlfrhgaE8OI3COBIHj8hE9i8G7CczdosXXfUHqfTOuMF5QMwwfm9AyAHeG+sNTWM/wymit59mExVwC5rG6bbfluH+P9GygubFICfjp1JClVRsHf2bpRwHrBG/bio4UCZrpJRDnQ96OCeStEMRX2xHyJqYH4zZJnOIISyqNJXoRzsGdQqAJaE88YwZsj3/1XEeOokRv6E0r80IWvgv2AMU96PdV5iNqCOh2CZECdf7WP6InGH61IFjO0sQcjMwbdVLTHHoAw==\"}",
         *     "code":"10000",
         *     "errorCode":"10000",
         *     "fileId":"A*krfPT7bSbY4AAAAAAAAAAAAADtp1AA",
         *     "msg":"Success",
         *     "params":{
         *         "biz_code":"content_creation"
         *     },
         *     "success":true
         * }
         */
        return response;
    }

    public static AlipayInsScenePetprofilePlatformprofileCheckResponse check(AlipayProfileCheckVO vo) {
        AlipayInsScenePetprofilePlatformprofileCheckRequest request = new AlipayInsScenePetprofilePlatformprofileCheckRequest();
        request.setBizContent(JSONObject.toJSONString(vo));
        AlipayInsScenePetprofilePlatformprofileCheckResponse response = null;
        try {
            response = (AlipayInsScenePetprofilePlatformprofileCheckResponse) getAlipayClient(config()).execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response == null || !response.isSuccess()) {
            System.out.println(String.format("method>>>{%s} error，request param :{%s}", request.getApiMethodName(), JSONObject.toJSONString(vo)));
        }
        return response;
    }

    public static AlipayBaseConfig config() {
        AlipayBaseConfig config = new AlipayBaseConfig();
        config.setAppId(AlipayProfileConstant.APPID);
        config.setPublicKey(AlipayProfileConstant.publicKey);
        config.setAlipayPublicKey(AlipayProfileConstant.alipayPublicKey);
        config.setPrivateKey(AlipayProfileConstant.privateKey);
        config.setAppAes(AlipayProfileConstant.aesKey);

        config.setAppId("2021002172654791");
        config.setPrivateKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCx4L89SahbFDEXTCGhIv22huwGcvL6UNglc1s9nmLPw/8jYLk0rEYO8gcw3P71ypbJUqP+EV2dAY4L6fDti6oGmO5HZiPXN6X2KFohOMdrUuoX+osJ8dXGAhTSBNlwFurL8mqa5fw8qbtFrz1cydULOZDBg3v8hwFUHwJeXRXJxT6VBa/Y/nH1iov1+6vLN1p40+wa3hq8Fk0Ay/bJCL/pAuBh0KdJFwerOnyLh9x4PR6BOIS64k/CA4p/WsLJNCPJ8PyfCf2t4M/Lg8yyNSy1F4F7j4tGiNXoD/z0UJkEfFSUjnxaaSzqv9ZrSK3DrrzIIOZskI9jEleeevnyUNK1AgMBAAECggEAbmjJa5pXxMjgu8xGp4VXpD9VK5+YECW0NHLI9JNmU/4dVPFJpFc2WTqDmiHio+Au/iGspxxSVg1MBTsdj+T8EYJFjM3qe0EQY52ibDKZHZXmtiGOwgp5HaHXGJFoAfpHnXYIE8OjcGgOVO+0D+87rO77WhJqGFIYUgW5a6ctygH6LHTC9BPuGuVywY/MvzsOjoNShE6gGGuByyTwd/+FHR6t5KTlKWVHBHVZoY0xwOZjUo7hPKvgqjgZYyG11xvQmeUBC5/0sybiqBrYZI4Fg9GwB/HYdEdgIbHR8qnQzf9HdtpuAgGwedAB+QdlCSlWo/rKmP3W9qpZeqbriTUfAQKBgQDjG20eI+6rWwvIttmWDglx/T6ijINlWzCwkqGu7d8wJem32fLyzcQ/unoms24RYzCongy+Yl2w7/Cr6eHfr1TVMj4U8EXOsFjTzGrk5A8hZOlPYJIprzUFOM3WGvI1LicU4aEuYlbgIwKBTv9vwgI881wtpRhFyfnfAp7mzrXXQQKBgQDIgf3RRPMQsBVkWHJz/dye3JMFh3mZYtzmY9CHpoBLGxjWWJynWJas61FnrftNCr1UKARcp53A0m8UW4r+JNsmJ4tdiYrOgLPRSOaE6wm32qkTwXn4kEYZikyE1AvSE3Bus6n37qvgIByP5q20vh0/w74hE8ptIEiYF/PGDzrydQKBgFtVCEj6wlz/PHn3rwF9m6bP5YSRZbY5OheIoKUs3HkMhjV93QpwXeATKlSuDDHJ5iOpjA50mKEznWJFqKTAailjDzx0dF5u5QooR0TNwpf3cSyO9bj10SjMc1sLcySX7vei1aNFwRJaggNmtatIJoZEiGmC34QRef+JmkXQE6kBAoGAP5Z6GFP1geVV42zpXz+zJ+5r8eT0+2APDkG5cEuthCQjzFZt1+SQZGZ8epUCjXxKbtYCDCBcNzqFX79oZRDrLz57RD7KdpL6c/TapmyjuFrwJAPH2rxmftMNUVKuLQFdNr5juO1INNv6MujFTfy86ev58COGnvUOEK4H8VgDKcECgYEArt96tbP5XPbyTQD1U+EY3kfPwpT+kI/l6ARcPgmf5mMbs+X1nSLK1dHOV69neGUSiUf3wRATzlgujqkSA0pjcux0aEGgN9pkNZ1ZkPAnQ2XYaCiwkj2Nu38PLApYklaV/f7/onPScJT3JgfkwHp2zWuqe4ObtUEQ5hhO+mj1IYk=");
        config.setAlipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlDnhX++ERS8DicCxH2zFz8m67MjTs2nNlwVq11FIdCnBIcsucQ5f+HjfDSisu7C+ay6Kbkb+/NO0xADtimCrWQutFuUXrdPr+ClLTDtnHOqbFuVPkREjSn8hxGuVtGoa9OzDDBb6gg7EXah6+g0ICYOGp3JXph671b3/Oim9LUN/2nrahrUElw51m5LTUK9BBdK4SZJrIDqP6abd5O+OjmPGoRT6f8gktJTsrdcK9IRAwSpqOKOZjV7eAnTHvCyNn7HbTkd7QLnIMrwFH8Si3yvcm6OZiLyPPV8p6po0mv/icfGMl/f2Tw9bJPZNzly8rVWLPkQ9xgFy1ReR6uNHqQIDAQAB");
        config.setAppAuthToken("202112BB178aa387edde40b5978e44ec7f384X29");
        config.setAppAes("xdoQhMn1EgytEOZk89IKEA==");
        return config;
    }

    /**
     * 获取支付客户端
     *
     * @return
     */
    public static AlipayClient getAlipayClient(AlipayBaseConfig config) {
        return new DefaultAlipayClient(AlipayBase.ALIPAY_GATEWAY, config.getAppId(), config.getPrivateKey(),
                AlipayBase.FORMAT_TYPE, AlipayBase.CHARSET, config.getAlipayPublicKey(), AlipayBase.SIGN_TYPE_RSA2);
    }


}
