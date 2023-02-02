package com.gmi.gameInfo.member.controller;

import com.gmi.gameInfo.member.domain.AuthEmail;
import com.gmi.gameInfo.member.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;


    @PostMapping("/authenticate")
    public ResponseEntity<?> sendAuthEmail(
            @RequestParam String email) {

        AuthEmail authEmail = AuthEmail.createAuthEmail(email, emailService.getAuthNum());
        emailService.sendAndSaveAuthEmail(authEmail);

        Map<String, Object> map = new HashMap<>();
        map.put("id", authEmail.getId());
        map.put("email", authEmail.getEmail());

        return ResponseEntity.ok(map);
    }

    @GetMapping("/verify-number/{id}")
    public ResponseEntity<?> verifyAuthenticateNumber(
            @PathVariable Long id,
            @RequestParam String authNum) {

        AuthEmail authEmail = emailService.findOneById(id);
        boolean checked = emailService.confirmAuthNum(authEmail, authNum);
        emailService.deleteById(id);

        return ResponseEntity.ok(checked);
    }
}
