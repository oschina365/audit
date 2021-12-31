package com.kz.audit.service.alipay;

import com.alipay.api.response.*;
import com.kz.audit.alipay.AlipayBaseConfig;
import com.kz.audit.vo.alipay.profile.*;

/**
 * 阿里，宠物识别服务
 *
 * @author kz
 * @date 2021-12-2 17:48:20
 */
public interface AlipayProfileService {

    /**
     * 配置
     *
     * @return
     */
    AlipayBaseConfig config();

    /**
     * 上传图片
     *
     * @param fileUrl
     * @return
     */
    String upload(String fileUrl);

    /**
     * 宠物照片校验
     *
     * @param vo
     * @return
     */
    AlipayInsScenePetprofilePlatformprofileCheckResponse check(AlipayProfileCheckVO vo);

    /**
     * 宠物建档
     *
     * @param vo
     * @return
     */
    AlipayInsScenePetprofilePlatformprofileCreateResponse create(AlipayProfileCreateVO vo);

    /**
     * 宠物档案查询
     *
     * @param petId
     * @return
     */
    AlipayInsScenePetprofilePlatformprofileQueryResponse query(String petId);

    /**
     * 宠物档案修改
     *
     * @param vo
     * @return
     */
    AlipayInsScenePetprofilePlatformprofileModifyResponse modify(AlipayProfileModifyVO vo);

    /**
     * 宠物档案删除
     *
     * @param petId
     * @return
     */
    AlipayInsScenePetprofilePlatformprofileDeleteResponse delete(String petId);

    /**
     * 宠物身份核验
     *
     * @param vo
     * @return
     */
    AlipayInsScenePetprofilePlatformprofileIdentifyResponse identify(AlipayIdentifyVO vo);

    /**
     * 宠物档案匹配
     *
     * @param vo
     * @return
     */
    AlipayInsScenePetprofilePlatformprofileMatchResponse match(AlipayMatchVO vo);

}
