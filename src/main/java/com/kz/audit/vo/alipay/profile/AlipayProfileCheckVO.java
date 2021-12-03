package com.kz.audit.vo.alipay.profile;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author kz
 * @date 2021-12-2 18:06:56
 */
@Data
public class AlipayProfileCheckVO {

    /**
     * 宠物类型，狗(2000)/猫(1000)
     */
    @JSONField(name = "pet_type")
    private Integer petType;

    /**
     * 宠物照片类型
     * NOSE("nose", "鼻纹照"),FACE("face", "正脸照");
     */
    @JSONField(name = "pet_photo_type")
    private String petPhotoType;

    /**
     * 文件上传后获取的ID
     */
    @JSONField(name = "file_id")
    private String fileId;
}
