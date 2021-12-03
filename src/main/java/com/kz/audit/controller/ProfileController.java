package com.kz.audit.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 萌宠识别接口
 *
 * @author kz
 * @since 2021-12-2 17:17:43
 */
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @PostMapping("check")
    @ResponseBody
    public void check(){

    }

}
