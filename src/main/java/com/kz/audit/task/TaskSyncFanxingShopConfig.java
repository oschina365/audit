package com.kz.audit.task;


import cn.hutool.json.JSONUtil;
import com.kz.audit.constants.fanxing.shop.ShopConfigKeyEnum;
import com.kz.audit.dao.alipay.AuditAlipayDAO;
import com.kz.audit.dao.alipay.vo.AlipayAppletConfigTO;
import com.kz.audit.dao.fanxing.shop.ShopConfigDAO;
import com.kz.audit.entity.alipay.AuditAlipayEntity;
import com.kz.audit.entity.db.DbQuery;
import com.kz.audit.entity.db.TransactionService;
import com.kz.audit.entity.fanxing.shop.ShopConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 同步支付宝配置
 *
 * @author kz
 * @date 2019年10月30日10:23:25
 */
@Component
public class TaskSyncFanxingShopConfig {

    /**
     * 每隔10分钟查询
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void timer() throws Exception {
        DbQuery.get("mysql").transaction(new TransactionService() {
            @Override
            public void execute() throws Exception {
                List<ShopConfig> list = ShopConfigDAO.ME.getByKey(ShopConfigKeyEnum.ALIPAY_APPLET_CONFIG.getKey());
                int totalCount = AuditAlipayEntity.ME.totalCount(" type=1 ");
                if (totalCount == list.size()) {
                    System.out.println(String.format("无需进行同步操作，商户数量[%s个商户]一致", totalCount));
                    return;
                }
                for (ShopConfig config : list) {
                    AlipayAppletConfigTO to = JSONUtil.toBean(config.getConfig(), AlipayAppletConfigTO.class);
                    /*if (to == null || StrUtil.isBlank(to.getAppAuthToken())) {
                        continue;
                    }*/
                    AuditAlipayEntity alipayEntity = AuditAlipayDAO.ME.getByToken(to.getAppAuthToken());
                    if (alipayEntity == null) {
                        alipayEntity = new AuditAlipayEntity();
                        alipayEntity.setName(to.getAppName());
                        alipayEntity.setType(1);
                        alipayEntity.setStatus(1);
                        alipayEntity.setCreate_time(new Date());
                        alipayEntity.setToken(to.getAppAuthToken());
                        alipayEntity.setAppid(to.getAppletId());
                        alipayEntity.setTenant_code(config.getTenant_code());
                        alipayEntity.save(false);
                        System.out.println(String.format("商户[%s]-商户号[%s]添加成功", to.getAppName(), config.getTenant_code()));
                    } else {
                        alipayEntity.setToken(to.getAppAuthToken());
                        alipayEntity.setAppid(to.getAppletId());
                        alipayEntity.setTenant_code(config.getTenant_code());
                        alipayEntity.doUpdate(false);
                        System.out.println(String.format("商户[%s]-商户号[%s]更新成功", to.getAppName(), config.getTenant_code()));
                    }
                }
            }
        });
    }

}
