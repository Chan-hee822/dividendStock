package com.example.dividendstock.web;

import com.example.dividendstock.model.Auth;
import com.example.dividendstock.model.MemberEntity;
import com.example.dividendstock.security.TokenProvider;
import com.example.dividendstock.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request) {
        // 회원 가입을 위한 API
        MemberEntity result = this.memberService.register(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request) {
        // 로그인하기 위한 API
        MemberEntity member = this.memberService.authenticate(request);
        String token = this.tokenProvider
                            .generateToken(member.getUsername(), member.getRoles());
        log.info("user login -> " + request.getUsername() );
        return ResponseEntity.ok(token);
    }

}
