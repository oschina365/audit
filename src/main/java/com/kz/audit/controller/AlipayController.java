package com.kz.audit.controller;


import com.alipay.api.response.AlipayOpenMiniVersionListQueryResponse;
import com.kz.audit.dao.AuditAlipayDAO;
import com.kz.audit.entity.ApiResult;
import com.kz.audit.entity.AuditAlipayEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author kz
 * @since 2021-11-05 11:12:57
 */
@RestController
@RequestMapping("/audit/alipay")
public class AlipayController {

    /**
     * 查询数据库中的商家列表
     * type=1,企颖商城
     * type=2,企颖券模板
     *
     * @param type
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResult qiyingList(@RequestParam int type, @RequestParam int page, @RequestParam int size) {
        return ApiResult.successWithObject(AuditAlipayDAO.ME.list(type, page, 1000));
    }

    /**
     * 查询单商户小程序的版本列表
     *
     * @param id
     * @return
     */
    @GetMapping("/versions/{id}")
    public ApiResult versions(@PathVariable long id) {
        AuditAlipayEntity entity = AuditAlipayDAO.ME.get(id);
        if (entity == null) {
            return ApiResult.failWithMessage("该商户不存在");
        }
        AlipayOpenMiniVersionListQueryResponse response = AuditAlipayDAO.ME.miniVersionListQuery(entity);
        return ApiResult.successWithObject(response.getAppVersionInfos());
    }

    /**
     * 添加支付宝商户
     *
     * @param entity
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ApiResult add(@RequestBody AuditAlipayEntity entity) {
        AuditAlipayDAO.ME.saveQiyingEntity(entity);
        return ApiResult.success();
    }

    /**
     * 全部商户小程序提审
     *
     * @return
     */
    @PostMapping("/batchAudit")
    @ResponseBody
    public ApiResult batchAudit() {
        AuditAlipayDAO.ME.batchAudit();
        return ApiResult.success();
    }

    /**
     * 单个商户小程序
     * 提审/上架/退回开发/下架
     *
     * @return
     */
    @PostMapping("/operate/{id}")
    @ResponseBody
    public ApiResult audit(@PathVariable long id, @RequestParam String appVersion, @RequestParam(required = false) String logo) {
        return AuditAlipayDAO.ME.operate(id, appVersion, logo);
    }

    /**
     * 单个商户小程序添加开发者
     *
     * @return
     */
    @PostMapping("/memberCreate/{id}")
    @ResponseBody
    public ApiResult memberCreate(@PathVariable long id, @RequestParam String phone) {
        return AuditAlipayDAO.ME.memberCreate(id, phone);
    }
}
