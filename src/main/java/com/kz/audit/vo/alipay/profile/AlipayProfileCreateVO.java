package com.kz.audit.vo.alipay.profile;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 宠物档案创建
 *
 * @author kz
 * @date 2021-12-3 15:24:48
 */
@Data
public class AlipayProfileCreateVO {

    /**
     * 外部业务单号，用于幂等处理，避免多次建档
     * 如：20211234567890
     */
    @JSONField(name = "out_biz_no")
    private String outBizNo;

    /**
     * 宠物类型：狗(2000)/猫(1000)
     */
    private String type;

    /**
     * 生日 档案生日不能早于2000年，且不能晚于当天
     * 如：2021-12-01 00:00:00
     */
    private String birthday;

    /**
     * 宠物昵称 不能包含字母、数字和中文以外的字符
     */
    private String nick;

    /**
     * 宠物性别 1 公; 2 母
     */
    private String gender;

    /**
     * 是否绝育，0 否；1 是；2 未知
     */
    private String sterilization;

    /**
     * 宠物照片列表
     */
    private Photos photos;

    @Data
    public class Photos {
        /**
         * 宠物照片地址
         */
        private String url;

        /**
         * 宠物照片类型，
         * NOSE("nose", "鼻纹照"),
         * FACE("face", "正脸照");
         */
        private String type;
    }
}
