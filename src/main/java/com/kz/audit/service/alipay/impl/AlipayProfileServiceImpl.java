package com.kz.audit.service.alipay.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.FileItem;
import com.alipay.api.request.AlipayInsScenePetprofilePlatformprofileCheckRequest;
import com.alipay.api.request.AlipayInsScenePetprofilePlatformprofileCreateRequest;
import com.alipay.api.request.AlipayInsScenePetprofilePlatformprofileQueryRequest;
import com.alipay.api.request.AlipayOpenFileUploadRequest;
import com.alipay.api.response.AlipayInsScenePetprofilePlatformprofileCheckResponse;
import com.alipay.api.response.AlipayInsScenePetprofilePlatformprofileCreateResponse;
import com.alipay.api.response.AlipayInsScenePetprofilePlatformprofileQueryResponse;
import com.alipay.api.response.AlipayOpenFileUploadResponse;
import com.kz.audit.alipay.AlipayBaseConfig;
import com.kz.audit.alipay.AlipayUtil;
import com.kz.audit.constants.alipay.AlipayProfileConstant;
import com.kz.audit.service.alipay.AlipayProfileService;
import com.kz.audit.vo.alipay.profile.AlipayProfileCheckVO;
import com.kz.audit.vo.alipay.profile.AlipayProfileCreateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 阿里，宠物识别服务
 *
 * @author kz
 * @date 2021-12-2 17:48:20
 */
@Slf4j
@Service
public class AlipayProfileServiceImpl implements AlipayProfileService {


    @Autowired
    private AlipayUtil alipayUtil;

    /**
     * 配置
     *
     * @return
     */
    @Override
    public AlipayBaseConfig config() {
        AlipayBaseConfig config = new AlipayBaseConfig();
        config.setAppId(AlipayProfileConstant.APPID);
        config.setPublicKey(AlipayProfileConstant.publicKey);
        config.setAlipayPublicKey(AlipayProfileConstant.alipayPublicKey);
        config.setPrivateKey(AlipayProfileConstant.privateKey);
        config.setAppAes(AlipayProfileConstant.aesKey);
        return config;
    }

    /**
     * 上传图片
     *
     * @param fileUrl
     * @return
     */
    @Override
    public String upload(String fileUrl) {
        AlipayOpenFileUploadRequest request = new AlipayOpenFileUploadRequest();
        request.setBizCode("content_creation");
        FileItem FileContent = new FileItem(fileUrl);
        request.setFileContent(FileContent);
        AlipayOpenFileUploadResponse response = (AlipayOpenFileUploadResponse) alipayUtil.execute(request, config());
        if (response == null || !response.isSuccess()) {
            log.error("method>>>{} error，request param :{}", request.getApiMethodName(), fileUrl);
        }
        return response.getFileId();
    }


    /**
     * 宠物照片校验
     *
     * @param vo
     * @return
     */
    @Override
    public AlipayInsScenePetprofilePlatformprofileCheckResponse check(AlipayProfileCheckVO vo) {
        AlipayInsScenePetprofilePlatformprofileCheckRequest request = new AlipayInsScenePetprofilePlatformprofileCheckRequest();
        request.setBizContent(JSONObject.toJSONString(vo));
        AlipayInsScenePetprofilePlatformprofileCheckResponse response = (AlipayInsScenePetprofilePlatformprofileCheckResponse) alipayUtil.execute(request, config());
        if (response == null || !response.isSuccess()) {
            log.error("method>>>{} error，request param :{}", request.getApiMethodName(), JSONObject.toJSONString(vo));
        }
        return response;
    }

    /**
     * 宠物建档
     *
     * @param vo
     * @return
     */
    @Override
    public AlipayInsScenePetprofilePlatformprofileCreateResponse create(AlipayProfileCreateVO vo) {
        AlipayInsScenePetprofilePlatformprofileCreateRequest request = new AlipayInsScenePetprofilePlatformprofileCreateRequest();
        request.setBizContent(JSONObject.toJSONString(vo));
        AlipayInsScenePetprofilePlatformprofileCreateResponse response = (AlipayInsScenePetprofilePlatformprofileCreateResponse) alipayUtil.execute(request, config());
        if (response == null || !response.isSuccess()) {
            log.error("method>>>{} error，request param :{}", request.getApiMethodName(), JSONObject.toJSONString(vo));
        }
        return response;
    }

    /**
     * 宠物档案查询
     *
     * @param petId
     * @return
     */
    @Override
    public AlipayInsScenePetprofilePlatformprofileQueryResponse query(String petId) {
        AlipayInsScenePetprofilePlatformprofileQueryRequest request = new AlipayInsScenePetprofilePlatformprofileQueryRequest();
        request.setBizContent("{\"pet_id\":\"" + petId + "\"}");
        AlipayInsScenePetprofilePlatformprofileQueryResponse response = (AlipayInsScenePetprofilePlatformprofileQueryResponse) alipayUtil.execute(request, config());
        if (response == null || !response.isSuccess()) {
            log.error("method>>>{} error，request param :{}", request.getApiMethodName(), petId);
        }
        return response;
    }
}
