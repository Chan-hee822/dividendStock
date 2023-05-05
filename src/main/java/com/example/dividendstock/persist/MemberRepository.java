package com.example.dividendstock.persist;

import com.example.dividendstock.model.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
     Optional<MemberEntity> findByUsername(String username);    // 아이디 기준으로 회원 정보 조회

    boolean existsByUsername(String username);  // 중복 아이디 체크
}
