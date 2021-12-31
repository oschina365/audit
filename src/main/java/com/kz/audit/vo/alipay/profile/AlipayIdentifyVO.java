package com.kz.audit.vo.alipay.profile;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author kz
 * @date 2021-12-4 10:45:03
 */
@Data
public class AlipayIdentifyVO {

    /**
     * 宠物ID
     */
    @JSONField(name = "pet_id")
    private String petId;

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
