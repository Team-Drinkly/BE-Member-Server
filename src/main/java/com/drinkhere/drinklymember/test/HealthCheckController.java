package com.drinkhere.drinklymember.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class HealthCheckController {

    @GetMapping("/health-check")
    public String status() {
        return "유저 마이크서비스 테스트입니다.";
    }
}
