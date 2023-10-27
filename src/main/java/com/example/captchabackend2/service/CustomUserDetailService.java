package com.example.captchabackend2.service;

import com.example.captchabackend2.entity.UserInfo;
import com.example.captchabackend2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService {
    @Autowired
    UserRepo userRepo;
    public UserInfo saveUserDetails(UserInfo userInfo){
        return userRepo.save(userInfo);
    }
}
