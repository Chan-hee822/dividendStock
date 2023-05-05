package com.example.dividendstock.persist.entity;

import com.example.dividendstock.model.Dividend;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "DIVIDEND")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 배당금 데이터가 중복으로 저장되는 것을 방지 -> 복합 유니크 키설정.
@Table(
		uniqueConstraints = {
				@UniqueConstraint(
						columnNames = {"companyId", "date"}
				)
		}
)
public class DividendEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long companyId;

	private LocalDateTime date;

	private String dividend;

	public DividendEntity(Long companyId, Dividend dividend) {
		this.companyId = companyId;
		this.date = dividend.getDate();
		this.dividend = dividend.getDividend();
	}
}
