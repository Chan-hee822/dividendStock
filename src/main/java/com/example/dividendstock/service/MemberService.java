package com.example.dividendstock.service;

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
            throw new RuntimeException("해당 아이디가 사용 중 입니다.");
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        MemberEntity result = this.memberRepository.save(member.toEntity());
        return result;

    }

    //로그인 할 때 검증 위한
    public MemberEntity authenticate(Auth.SignIn member ) {
        return null;
    }
}
