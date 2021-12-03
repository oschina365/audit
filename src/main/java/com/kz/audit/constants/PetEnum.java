package com.kz.audit.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 宠物枚举
 *
 * @author kz
 * @date 2021-12-2 17:55:38
 */
public class PetEnum {

    /**
     * 宠物类型
     */
    @Getter
    @AllArgsConstructor
    public enum TYPE {
        CAT(1000, "cat", "猫"),
        DOG(2000, "dog", "狗");

        private int code;
        private String type;
        private String desc;


    }

    /**
     * 宠物照片类型
     */
    @Getter
    @AllArgsConstructor
    public enum PHOTO_TYPE {
        NOSE("nose", "鼻纹照"),
        FACE("face", "正脸照");

        private String type;
        private String desc;


    }


}
