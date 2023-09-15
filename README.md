# Dividend Stock Application

Dividend Stock Application은 주식 관련 정보와 배당금 정보를 관리하기 위한 웹 어플리케이션입니다. 이 어플리케이션은 Spring Boot와 Spring Security를 기반으로 개발되었습니다.

## 기능

1. 회원 가입 및 로그인
- 사용자는 회원 가입을 할 수 있으며, 가입한 사용자는 로그인을 통해 어플리케이션에 액세스할 수 있습니다.
2. 주식 정보 검색
- 사용자는 주식 회사의 티커(ticker)를 입력하여 해당 주식 회사의 정보와 배당금 정보를 검색할 수 있습니다.
3. 회사 정보 및 배당금 정보 저장
- 사용자는 주식 회사의 티커를 입력하여 해당 회사의 정보를 웹 스크래핑을 통해 가져올 수 있습니다. 이 정보는 데이터베이스에 저장되고, 배당금 정보도 함께 저장됩니다.
4. 회사 정보 자동 완성
- 사용자가 주식 회사의 이름을 입력할 때 자동 완성 기능을 제공합니다.
5. 회사 정보 삭제
- 사용자는 주식 회사의 티커를 입력하여 해당 회사의 정보와 배당금 정보를 삭제할 수 있습니다.

## 사용 기술

- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf (템플릿 엔진)
- Redis (캐싱)
- H2 Database (개발용 데이터베이스)
- JSoup (웹 스크래핑)
- Quartz Scheduler (스케줄링)
- JWT (JSON Web Tokens, 인증 기능)

## 시큐리티 설정

Spring Security를 사용하여 어플리케이션의 보안을 설정했습니다. 주요 설정 내용은 다음과 같습니다.

- 회원 가입 및 로그인은 JWT (JSON Web Tokens) 기반으로 보호됩니다.
- 주식 정보 검색과 회사 정보 관리 기능은 인증이 필요한 보호된 엔드포인트입니다.
- H2 콘솔은 개발용으로만 사용되며, 시큐리티 설정에서 접근이 허용되었습니다.

## 예외 처리

어플리케이션은 몇 가지 예외 상황을 처리합니다. 각 예외에 대한 설명은 다음과 같습니다.

1. 접근 권한이 없는 사용자 예외 (AccessDeniedException): 사용자가 특정 리소스에 액세스할 권한이 없는 경우 발생합니다.

2. 이미 존재하는 사용자명 예외 (AlreadyExistUserException): 이미 존재하는 사용자명을 사용하여 회원 가입하려고 할 때 발생합니다.

3. 존재하지 않는 회사명 예외 (NoCompanyException): 존재하지 않는 주식 회사의 티커를 입력하여 검색하려고 할 때 발생합니다.

## 추가 작업

1. 예외 메시지와 함께 로깅을 통해 디버깅 및 문제 해결을 용이하게 만들기.
2. 성능 최적화 위해 스크래퍼와 스케줄러에서 발생하는 대용량 데이터 처리를 위해 비동기 작업을 고려하기.
