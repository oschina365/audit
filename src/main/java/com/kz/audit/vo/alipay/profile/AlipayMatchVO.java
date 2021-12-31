package com.kz.audit.vo.alipay.profile;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 宠物档案匹配
 *
 * @author kz
 * @date 2021-12-4 10:45:03
 */
@Data
public class AlipayMatchVO {

    /**
     * 宠物类型：狗(2000)/猫(1000)
     */
    @JSONField(name = "pet_type")
    private String petType;

    /**
     * 宠物照片列表
     */
    private AlipayProfileModifyVO.Photos photos;

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
