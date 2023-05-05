package com.example.dividendstock.config;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

	@Bean
	public Trie<String, String> trie() {
		return new PatriciaTrie<>();
		// 인스턴스를 각각 생성하고 공유되는 변수
		// 싱글톤으로 움직이게
		// 여기서 생성된 트라이 빈이 초기화 될 때 컴파니 서비스에 주입이되고
		// 컴파니 서비스의 트라이 인스턴스로 사용할 수 있게끔.
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
