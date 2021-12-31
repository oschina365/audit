package com.kz.audit.dao;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.FileItem;
import com.alipay.api.domain.AppVersionInfo;
import com.alipay.api.domain.RegionInfo;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.kz.audit.alipay.*;
import com.kz.audit.constants.alipay.AlipayAppVersionEnum;
import com.kz.audit.constants.alipay.AlipayIsvConstant;
import com.kz.audit.entity.ApiResult;
import com.kz.audit.entity.AuditAlipayEntity;
import com.kz.audit.util.FileUtils;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author kz
 * @since 2021-11-05 11:15:55
 */
@Log
public class AuditAlipayDAO extends CommonDao<AuditAlipayEntity> {

    public static final AuditAlipayDAO ME = new AuditAlipayDAO();
    public static final String bundleId = "com.alipay.alipaywallet";

    @Override
    protected String databaseName() {
        return "mysql";
    }

    /**
     * 查询单个商户数据
     *
     * @param id
     * @return
     */
    public AuditAlipayEntity get(long id) {
        String sql = "select * from " + AuditAlipayEntity.ME.tableName() + " where id =?";
        return getDbQuery().read(AuditAlipayEntity.class, sql, id);
    }

    /**
     * 分页查询商户
     *
     * @param type
     * @param page
     * @param size
     * @return
     */
    public List<AuditAlipayEntity> list(int type, int page, int size) {
        String sql = "select * from " + AuditAlipayEntity.ME.tableName() + " where type=? order by create_time desc";
        return getDbQuery().query_slice(AuditAlipayEntity.class, sql, page, size, type);
    }

    /**
     * 添加商户
     *
     * @param entity
     */
    public void saveQiyingEntity(AuditAlipayEntity entity) {
        entity.setStatus(1);
        entity.setCreate_time(new Date());
        entity.save(false);
    }

    /**
     * 全部商户的小程序提交审核
     */
    @Async
    public void batchAudit() {
        List<AuditAlipayEntity> list = (List<AuditAlipayEntity>) AuditAlipayEntity.ME.list(false);
        if (list.isEmpty()) {
            return;
        }
        for (AuditAlipayEntity entity : list) {
            if (entity == null || StringUtils.isBlank(entity.getToken())) {
                continue;
            }
            String appVersion = getMaxInitAppVersion(entity);
            if (appVersion == null) {
                continue;
            }
            boolean flag = false;//apply(entity, appVersion, logo);
            if (flag) {
                log.warning(String.format("商户[%s]提交审核失败！", entity.getName()));
            }
        }
    }

    /**
     * 单个商户的小程序
     * 提审/上架/退回开发/下架
     *
     * @param id
     * @param appVersion
     * @param logo
     * @return
     */
    public ApiResult operate(long id, String appVersion, String logo) {
        AuditAlipayEntity entity = get(id);
        if (entity == null) {
            return ApiResult.failWithMessage("该商户不存在");
        }
        if (StringUtils.isBlank(entity.getToken())) {
            return ApiResult.failWithMessage("授权token为空");
        }
        String currentVersionStatus = getVersionStatus(entity, appVersion);
        String msg = "";
        if (StringUtils.equalsIgnoreCase(currentVersionStatus, AlipayAppVersionEnum.INIT.getKey())) {
            apply(entity, appVersion, logo);
            msg = appVersion + " 提审成功";
        }
        if (StringUtils.equalsIgnoreCase(currentVersionStatus, AlipayAppVersionEnum.WAIT_RELEASE.getKey())) {
            online(entity, appVersion);
            msg = appVersion + " 上架成功";
        }
        if (StringUtils.equalsIgnoreCase(currentVersionStatus, AlipayAppVersionEnum.AUDIT_REJECT.getKey())) {
            auditedCancel(entity, appVersion);
            msg = appVersion + " 版本撤回到开发版成功";
        }
        if (StringUtils.equalsIgnoreCase(currentVersionStatus, AlipayAppVersionEnum.RELEASE.getKey())) {
            offline(entity, appVersion);
            msg = appVersion + " 下架成功";
        }
        return ApiResult.success(msg);
    }

    /**
     * 获取对应的版本状态
     *
     * @param entity
     * @param appVersion
     * @return
     */
    public String getVersionStatus(AuditAlipayEntity entity, String appVersion) {
        AlipayOpenMiniVersionListQueryResponse response = miniVersionListQuery(entity);
        List<AppVersionInfo> versionInfoList = response.getAppVersionInfos();
        AppVersionInfo info = versionInfoList.stream().filter(e -> StringUtils.equalsIgnoreCase(e.getAppVersion(), appVersion)).findFirst().orElse(null);
        return info.getVersionStatus();
    }

    /**
     * 获取最大的版本号
     *
     * @return
     */
    public String getMaxInitAppVersion(AuditAlipayEntity entity) {
        AlipayOpenMiniVersionListQueryResponse response = miniVersionListQuery(entity);
        if (response == null) {
            log.warning(String.format("商户[%s]版本列表查询失败", entity.getName()));
            return null;
        }
        List<AppVersionInfo> versionInfoList = response.getAppVersionInfos();
        if (versionInfoList.isEmpty()) {
            log.warning(String.format("商户[%s]版本列表为空！", entity.getName()));
            return null;
        }
        AppVersionInfo info = versionInfoList.stream().filter(e -> StringUtils.equalsIgnoreCase(e.getVersionStatus(), "INIT")).findFirst().orElse(null);
        if (info == null) {
            log.warning(String.format("商户[%s]暂无开发版！", entity.getName()));
            return null;
        }
        return info.getVersionStatus();
    }

    /**
     * 单个商户小程序版本列表
     *
     * @param entity
     * @return
     */
    public AlipayOpenMiniVersionListQueryResponse miniVersionListQuery(AuditAlipayEntity entity) {
        AlipayBaseConfig conf = aliConfig(entity);
        if (StringUtils.isBlank(conf.getAppAuthToken())) {
            return null;
        }
        List<String> statusList = Stream.of(AlipayOpenMiniVersionStatusEnum.values()).
                map(AlipayOpenMiniVersionStatusEnum::getCode).collect(Collectors.toList());
        AlipayOpenMiniVersionListQueryRequest queryRequest = new AlipayOpenMiniVersionListQueryRequest();
        AlipayOpenMiniVersionListQueryBizRequest bizRequest = new AlipayOpenMiniVersionListQueryBizRequest();
        bizRequest.setBundleId(bundleId);
        bizRequest.setVersionStatus(StringUtils.join(statusList, ","));
        queryRequest.setBizContent(JSONObject.toJSONString(bizRequest));
        if (null != conf.getAppAuthToken() && !"".equals(conf.getAppAuthToken())) {
            queryRequest.putOtherTextParam("app_auth_token", conf.getAppAuthToken());
        }
        AlipayOpenMiniVersionListQueryResponse response = (AlipayOpenMiniVersionListQueryResponse) new AlipayUtil().execute(queryRequest, conf, false);
        System.out.println(JSONObject.toJSONString(response));
        return response;
    }

    /**
     * 撤回到开发版本
     *
     * @param entity
     * @param appVersion
     * @return
     */
    public boolean auditedCancel(AuditAlipayEntity entity, String appVersion) {
        AlipayBaseConfig config = aliConfig(entity);
        AlipayOpenMiniVersionAuditedCancelRequest deleteRequest = new AlipayOpenMiniVersionAuditedCancelRequest();
        AlipayOpenMiniAppVersionBizRequest bizRequest = new AlipayOpenMiniAppVersionBizRequest();
        bizRequest.setAppVersion(appVersion);
        bizRequest.setBundleId(bundleId);
        deleteRequest.setBizContent(JSONObject.toJSONString(bizRequest));
        if (null != config.getAppAuthToken() && !"".equals(config.getAppAuthToken())) {
            deleteRequest.putOtherTextParam("app_auth_token", config.getAppAuthToken());
        }
        AlipayOpenMiniVersionAuditedCancelResponse execute = (AlipayOpenMiniVersionAuditedCancelResponse) new AlipayUtil().execute(deleteRequest, config, false);
        if (!execute.isSuccess()) {
            System.out.println(String.format("撤回到开发版本失败,商户=%s", entity.getName()));
            return false;
        }
        System.out.println(JSONObject.toJSONString(execute));
        return true;
    }

    /**
     * 小程序生成体验版
     *
     * @param entity
     * @param appVersion
     * @return
     */
    public boolean experienceCreate(AuditAlipayEntity entity, String appVersion) {
        AlipayBaseConfig config = aliConfig(entity);
        AlipayOpenMiniExperienceCreateRequest deleteRequest = new AlipayOpenMiniExperienceCreateRequest();
        AlipayOpenMiniAppVersionBizRequest bizRequest = new AlipayOpenMiniAppVersionBizRequest();
        bizRequest.setAppVersion(appVersion);
        bizRequest.setBundleId(bundleId);
        deleteRequest.setBizContent(JSONObject.toJSONString(bizRequest));
        if (null != config.getAppAuthToken() && !"".equals(config.getAppAuthToken())) {
            deleteRequest.putOtherTextParam("app_auth_token", config.getAppAuthToken());
        }
        AlipayOpenMiniExperienceCreateResponse execute = (AlipayOpenMiniExperienceCreateResponse) new AlipayUtil().execute(deleteRequest, config, false);
        if (!execute.isSuccess()) {
            System.out.println(String.format("小程序生成体验版失败,商户=%s", entity.getName()));
            return false;
        }
        System.out.println(JSONObject.toJSONString(execute));
        return true;
    }

    /**
     * 小程序体验版状态查询接口
     *
     * @param entity
     * @param appVersion
     * @return
     */
    public AlipayOpenMiniExperienceQueryResponse miniExperienceQuery(AuditAlipayEntity entity, String appVersion) {
        AlipayBaseConfig config = aliConfig(entity);
        AlipayOpenMiniExperienceQueryRequest queryRequest = new AlipayOpenMiniExperienceQueryRequest();
        AlipayOpenMiniAppVersionBizRequest bizRequest = new AlipayOpenMiniAppVersionBizRequest();
        bizRequest.setAppVersion(appVersion);
        bizRequest.setBundleId(bundleId);
        queryRequest.setBizContent(JSONObject.toJSONString(bizRequest));
        if (null != config.getAppAuthToken() && !"".equals(config.getAppAuthToken())) {
            queryRequest.putOtherTextParam("app_auth_token", config.getAppAuthToken());
        }
        AlipayOpenMiniExperienceQueryResponse execute = (AlipayOpenMiniExperienceQueryResponse) new AlipayUtil().execute(queryRequest, config, false);
        if (!execute.isSuccess()) {
            System.out.println(String.format("小程序生成体验版失败,商户=%s", entity.getName()));
            return null;
        }
        System.out.println(JSONObject.toJSONString(execute));
        return execute;
    }

    /**
     * 下架
     *
     * @param entity
     * @param appVersion
     * @return
     */
    public boolean offline(AuditAlipayEntity entity, String appVersion) {
        AlipayBaseConfig config = aliConfig(entity);
        AlipayOpenMiniVersionOfflineRequest offlineRequest = new AlipayOpenMiniVersionOfflineRequest();
        AlipayOpenMiniAppVersionBizRequest bizRequest = new AlipayOpenMiniAppVersionBizRequest();
        bizRequest.setAppVersion(appVersion);
        bizRequest.setBundleId(bundleId);
        offlineRequest.setBizContent(JSONObject.toJSONString(bizRequest));
        if (null != config.getAppAuthToken() && !"".equals(config.getAppAuthToken())) {
            offlineRequest.putOtherTextParam("app_auth_token", config.getAppAuthToken());
        }
        AlipayOpenMiniVersionOfflineResponse execute = (AlipayOpenMiniVersionOfflineResponse) new AlipayUtil().execute(offlineRequest, config, false);
        if (!execute.isSuccess()) {
            System.out.println(String.format("撤回到开发版本失败,商户=%s", entity.getName()));
            return false;
        }
        System.out.println(JSONObject.toJSONString(execute));
        return true;
    }

    /**
     * 上架小程序
     *
     * @param entity
     * @param appVersion
     * @return
     */
    public boolean online(AuditAlipayEntity entity, String appVersion) {
        AlipayBaseConfig config = aliConfig(entity);
        AlipayOpenMiniVersionOnlineRequest onlineRequest = new AlipayOpenMiniVersionOnlineRequest();
        AlipayOpenMiniAppVersionBizRequest bizRequest = new AlipayOpenMiniAppVersionBizRequest();
        bizRequest.setAppVersion(appVersion);
        bizRequest.setBundleId(bundleId);
        onlineRequest.setBizContent(JSONObject.toJSONString(bizRequest));
        if (null != config.getAppAuthToken() && !"".equals(config.getAppAuthToken())) {
            onlineRequest.putOtherTextParam("app_auth_token", config.getAppAuthToken());
        }
        AlipayOpenMiniVersionOnlineResponse execute = (AlipayOpenMiniVersionOnlineResponse) new AlipayUtil().execute(onlineRequest, config, false);
        if (!execute.isSuccess()) {
            System.out.println(String.format("上架失败,商户=%s", entity.getName()));
            return false;
        }
        System.out.println(JSONObject.toJSONString(execute));
        return true;
    }

    /**
     * 提交审核
     *
     * @param entity
     * @param version
     * @param logo
     * @return
     */
    public boolean apply(AuditAlipayEntity entity, String version, String logo) {
        AlipayBaseConfig config = aliConfig(entity);
        AlipayOpenMiniVersionAuditApplyRequest request = new AlipayOpenMiniVersionAuditApplyRequest();
        request.setAppVersion(version);
        request.setRegionType("CHINA");
        request.setVersionDesc("提交新版本，审核人员辛苦了，麻烦审核下，谢谢");
        RegionInfo regionInfo = new RegionInfo();
        regionInfo.setProvinceCode("310000");
        regionInfo.setProvinceName("浙江省");
        regionInfo.setCityCode("310000");
        regionInfo.setCityName("杭州市");
        regionInfo.setAreaCode("311100");
        regionInfo.setAreaName("余杭区");
        request.setServiceRegionInfo(new ArrayList<RegionInfo>() {{
            add(regionInfo);
        }});

        if (entity.getType() == 2) {
           /* try {
                InputStream inputStream = FileUtils.resizeImage(logo.getInputStream(), 180, 180);
                request.setAppLogo(new FileItem(entity.getName() + "_logo", FileUtils.readInputStream(inputStream)));
            } catch (Exception e) {
                e.printStackTrace();
            }*/

           /* InputStream inputStream = null;
            try {
                inputStream = FileUtils.resizeImage(FileUtils.getFileStreamFromUrl(logo), 180, 180);
                request.setAppLogo(new FileItem(entity.getName(), FileUtils.readInputStream(inputStream)));
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            String logoPath = FileUtils.downloadImage(logo);
            System.out.println(logoPath);
            File logoFile = new File(logoPath);
            // FileItem logo1 = new FileItem("logo", FileUtils.getFileStream(logo), "image/png");
            System.out.println(logoFile.exists());
            request.setAppLogo(new FileItem(logoFile));

            request.setAppSlogan("商户提供领券的小程序");
            //企颖券模板提审
            //File tmpFile = new File("src/main/resources/qiying_coupon/audit.rar");
            File tmpFile = new File("/download/qiying_coupon/audit.rar");
            request.setTestFileName(new FileItem(tmpFile));
            request.setAppDesc("为商户提供领券的小程序，商户在支付宝运营中心配置的券，可以显示在此款小程序上，供消费者进行领取，然后线下支付，打开支付宝付款码核销领取的优惠券。");
            //工具_效率  XS1014_XS2117
            request.setMiniCategoryIds("XS1014_XS2117");

            /*File first = new File("src/main/resources/qiying_coupon/screen1.png");
            File second = new File("src/main/resources/qiying_coupon/screen2.png");*/
            File first = new File("/download/qiying_coupon/screen1.png");
            File second = new File("/download/qiying_coupon/screen2.png");
            request.setFirstScreenShot(new FileItem(first));
            request.setSecondScreenShot(new FileItem(second));
        }
        if (entity.getType() == 1) {
            String logoPath = FileUtils.downloadImage(logo);
            System.out.println(logoPath);
            File logoFile = new File(logoPath);
            // FileItem logo1 = new FileItem("logo", FileUtils.getFileStream(logo), "image/png");
            System.out.println(logoFile.exists());
            request.setAppLogo(new FileItem(logoFile));
            request.setAppSlogan("支付宝商城");
            //零售电商_大型超市  XS1020_XS2169
            request.setMiniCategoryIds("XS1020_XS2169");
            request.setAppDesc("支付宝小程序商城");
            String first = "http://img.oscer.net/qiying/qiying_shop_first_screen.png";
            String second = "http://img.oscer.net/qiying/qiying_shop_second_screen.png";
            request.setFirstScreenShot(new FileItem("qiying_shop_first_screen", FileUtils.getFileStream(first)));
            request.setSecondScreenShot(new FileItem("qiying_shop_second_screen", FileUtils.getFileStream(second)));
        }

        request.putOtherTextParam("app_auth_token", config.getAppAuthToken());
        AlipayOpenMiniVersionAuditApplyResponse execute = (AlipayOpenMiniVersionAuditApplyResponse) new AlipayUtil().execute(request, config, false);
        if (!execute.isSuccess()) {
            System.out.println(String.format("提交审核失败,商户=%s", entity.getName()));
            return false;
        }
        System.out.println(JSONObject.toJSONString(execute));
        return true;
    }

    /**
     * 添加小程序成员
     *
     * @param id
     * @return
     */
    public ApiResult memberCreate(long id, String phone) {
        AuditAlipayEntity entity = get(id);
        AlipayBaseConfig config = aliConfig(entity);
        AlipayOpenAppMembersCreateBizRequest bizRequest = new AlipayOpenAppMembersCreateBizRequest();
        bizRequest.setLogon_id(StringUtils.isBlank(phone) ? "18671310745" : phone);
        bizRequest.setRole("DEVELOPER");
        AlipayOpenAppMembersCreateRequest request = new AlipayOpenAppMembersCreateRequest();
        request.setBizContent(JSONObject.toJSONString(bizRequest));
        if (null != config.getAppAuthToken() && !"".equals(config.getAppAuthToken())) {
            request.putOtherTextParam("app_auth_token", config.getAppAuthToken());
        }
        AlipayOpenAppMembersCreateResponse execute = (AlipayOpenAppMembersCreateResponse) new AlipayUtil().execute(request, config);
        if (!execute.isSuccess()) {
            System.out.println(String.format("添加小程序成员失败,商户=%s", entity.getName()));
            return ApiResult.failWithMessage("添加小程序成员失败");
        }
        System.out.println(JSONObject.toJSONString(execute));
        return ApiResult.success();
    }


    /**
     * 阿里配置
     *
     * @param entity
     * @return
     */
    public AlipayBaseConfig aliConfig(AuditAlipayEntity entity) {
        if (entity == null || StringUtils.isBlank(entity.getToken())) {
            return null;
        }
        AlipayBaseConfig config = new AlipayBaseConfig();
        config.setAppId(AlipayIsvConstant.QiYing.appId);
        config.setPrivateKey(AlipayIsvConstant.QiYing.privateKey);
        config.setAlipayPublicKey(AlipayIsvConstant.QiYing.alipayPublicKey);

        if (entity.getType() == 2) {
            config.setAppId(AlipayIsvConstant.Coupon.appId);
            config.setPrivateKey(AlipayIsvConstant.Coupon.privateKey);
            config.setAlipayPublicKey(AlipayIsvConstant.Coupon.alipayPublicKey);
        }
        config.setAppAuthToken(entity.getToken());
        return config;
    }

}
