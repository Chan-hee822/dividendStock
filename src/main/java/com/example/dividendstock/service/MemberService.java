package com.example.dividendstock.service;

import com.example.dividendstock.exception.impl.AlreadyExistUserException;
import com.example.dividendstock.model.Auth;
import com.example.dividendstock.model.MemberEntity;
import com.example.dividendstock.persist.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    // 비밀번호(민감정보)는 날 것 그대로 DB 저장시 보안 이슈가 생김 인코딩 한 번 해줄 필요 O
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Couldn't find user -> " + username));

    }

    // 회원가입
    public MemberEntity register(Auth.SignUp member) {
        boolean exists = this.memberRepository
                                .existsByUsername(member.getUsername());

        if(exists){
            throw new AlreadyExistUserException();
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        return this.memberRepository.save(member.toEntity());

    }

    //로그인 할 때 검증 위한 - password 인증
    public MemberEntity authenticate(Auth.SignIn member ) {
        MemberEntity user = this.memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID 입니다."));
        // user 에 들어있는 password 는 인코딩한 값 (참고)

        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        return user;
    }
}
