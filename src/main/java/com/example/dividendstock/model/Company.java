/*
서비스 코드 작성을 위한 모델 클래스
데이터를 주고 받기 위한 용도
entity 클래스는 데이터베이스와 직접적으로 매핑을 하기 위한 클래스
entity 클래스를 데이터를 주고 받거나 수정을 하면 원래 역할 범위를 벗어남.
계좌시스템 프로젝트에서 DTO 역할.
 */

package com.example.dividendstock.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
	private String ticker;
	private String name;
}
