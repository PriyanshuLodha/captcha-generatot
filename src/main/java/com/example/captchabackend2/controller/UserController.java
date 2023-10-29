package com.example.captchabackend2.controller;

import cn.apiclub.captcha.Captcha;
import com.example.captchabackend2.entity.CaptchaData;
import com.example.captchabackend2.entity.UserInfo;
import com.example.captchabackend2.service.CaptchaGenerator;
import com.example.captchabackend2.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class UserController {
    private String permanentCaptcha;
    @Autowired
    CustomUserDetailService customUserDetailService;
    CaptchaGenerator captchaGenerator;
    @GetMapping("/generateCaptcha")
    public String generateCaptcha(){
        CaptchaData captchaData=new CaptchaData();
        Captcha captcha= CaptchaGenerator.generateCaptcha(120,40);
        captchaData.setHiddenCaptcha(captcha.getAnswer());
        permanentCaptcha =captcha.getAnswer();
        captchaData.setCaptcha("");
        captchaData.setRealCaptcha(CaptchaGenerator.encodeCaptchatoBinary(captcha));
        return CaptchaGenerator.encodeCaptchatoBinary(captcha);
    }
    @PostMapping("/signup/register")
    public String createUser(@RequestBody UserInfo userInfo){
        if(userInfo.getCaptcha().equals(permanentCaptcha)){
            customUserDetailService.saveUserDetails(userInfo);
            return "success";
        }
        else {
            return "please enter valid captcha";
        }
    }
    @GetMapping("/signup/getNotes")
    public
}
