package com.drinkhere.drinklymember.test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class HealthCheckController {

    @GetMapping("/health-check")
    public String status() {
        return "유저 마이크서비스 테스트입니다.";
    }

    @GetMapping("/me")
    public ResponseEntity<String> getUserId(@RequestHeader(value = "user-id", required = false) String userId) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("미인증 상태입니다.");
        }
        return ResponseEntity.ok("User ID : " + userId);
    }
}
