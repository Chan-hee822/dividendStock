package com.example.dividendstock.service;

import com.example.dividendstock.exception.impl.NoCompanyException;
import com.example.dividendstock.model.Company;
import com.example.dividendstock.model.Dividend;
import com.example.dividendstock.model.ScrapedResult;
import com.example.dividendstock.model.constants.CacheKey;
import com.example.dividendstock.persist.CompanyRepository;
import com.example.dividendstock.persist.DividendRepository;
import com.example.dividendstock.persist.entity.CompanyEntity;
import com.example.dividendstock.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;

	// 요청이 자주 들어오는가?
	// 자주 변경되는 데이터?
	@Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
	public ScrapedResult getDividendByCompanyName(String companyName) {

		// 1. 회사명을 기준으로 회사정보를 조회
		CompanyEntity company = this.companyRepository.findByName(companyName)
				.orElseThrow(() -> new NoCompanyException());
//				 null 값이 아니면 감싼 optional을 벗기고 반환해주고, null이면 에러 발생

		// 2. 조회한 화사 ID 로 배당금 정보 조회
		List<DividendEntity> dividendEntities =
				this.dividendRepository.findAllByCompanyId(company.getId());

		// 3. 회사정보 + 배당금 정보 조합 후 반환
		// scapeResult -> company 와 list<dividend> 을 인자로 받음
		// entity 를 일반 모델 클래스로 매핑하는 작업이 필요
		List<Dividend> dividends = new ArrayList<>();
		for (var entity : dividendEntities) {
			dividends.add(Dividend.builder()
					.dividend(entity.getDividend())
					.date(entity.getDate())
					.build()
			);
		}
		List<Dividend> dividends1 = dividendEntities.stream()
				.map(e -> Dividend.builder()
						.date(e.getDate())
						.dividend(e.getDividend())
						.build())
				.collect(Collectors.toList());

		return new ScrapedResult(Company.builder()
				.ticker(company.getTicker())
				.name(company.getName())
				.build(), dividends);
	}
}
