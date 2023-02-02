package com.gmi.gameInfo.member.controller;

import com.gmi.gameInfo.member.domain.AuthEmail;
import com.gmi.gameInfo.member.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;


    @PostMapping("/api/email/authenticate")
    public ResponseEntity<?> sendAuthEmail(
            @RequestParam String email) {

        AuthEmail authEmail = AuthEmail.createAuthEmail(email, emailService.getAuthNum());
        emailService.sendAndSaveAuthEmail(authEmail);

        Map<String, Object> map = new HashMap<>();
        map.put("id", authEmail.getId());
        map.put("email", authEmail.getEmail());

        return ResponseEntity.ok(map);
    }
}
