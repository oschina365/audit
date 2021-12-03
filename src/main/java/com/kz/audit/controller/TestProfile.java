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
import com.kz.audit.constants.PetEnum;
import com.kz.audit.constants.alipay.AlipayProfileConstant;
import com.kz.audit.util.FileUtils;
import com.kz.audit.vo.alipay.profile.AlipayProfileCheckVO;

public class TestProfile {

    public static void main(String[] args) {
        /*String fileUrl = "http://img.oscer.net/alipay/profile/dog/dog1.jpg";
        System.out.println(JSONObject.toJSONString(profileUpload(fileUrl)));*/

        AlipayProfileCheckVO vo = new AlipayProfileCheckVO();
        vo.setFileId("A*krfPT7bSbY4AAAAAAAAAAAAADtp1AA");
        vo.setPetType(PetEnum.TYPE.DOG.getCode());
        vo.setPetPhotoType(PetEnum.PHOTO_TYPE.NOSE.getType());
        System.out.println(JSONObject.toJSONString(check(vo)));
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
