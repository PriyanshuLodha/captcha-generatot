package com.example.captchabackend2.controller;

import cn.apiclub.captcha.Captcha;
import com.example.captchabackend2.entity.CaptchaData;
import com.example.captchabackend2.service.CaptchaGenerator;
import com.example.captchabackend2.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private String permanentCaptcha;
    @Autowired
    CustomUserDetailService customUserDetailService;
    CaptchaGenerator captchaGenerator;
    @GetMapping("/generateCaptcha")
    public String generateCaptcha(){
        CaptchaData captchaData=new CaptchaData();
        Captcha captcha= CaptchaGenerator.generateCaptcha(260,80);
        captchaData.setHiddenCaptcha(captcha.getAnswer());
        permanentCaptcha =captcha.getAnswer();
        captchaData.setCaptcha("");
        captchaData.setRealCaptcha(CaptchaGenerator.encodeCaptchatoBinary(captcha));
        return CaptchaGenerator.encodeCaptchatoBinary(captcha);
    }

}
