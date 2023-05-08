package com.example.dividendstock.scheduler;

import com.example.dividendstock.model.Company;
import com.example.dividendstock.model.ScrapedResult;
import com.example.dividendstock.model.constants.CacheKey;
import com.example.dividendstock.persist.CompanyRepository;
import com.example.dividendstock.persist.DividendRepository;
import com.example.dividendstock.persist.entity.CompanyEntity;
import com.example.dividendstock.persist.entity.DividendEntity;
import com.example.dividendstock.scraper.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler {

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;

	private final Scraper yahooFinanceScraper;

//	@Scheduled(fixedDelay = 1000)
//	public void test1() throws InterruptedException {
//		Thread.sleep(10000);
//		System.out.println(Thread.currentThread().getName() + " no.1 " + LocalDateTime.now());
//	}
//
//	@Scheduled(fixedDelay = 1000)
//	public void test2() throws InterruptedException {
//		System.out.println(Thread.currentThread().getName() + " no.2 " + LocalDateTime.now());
//	}

	/*
		일정 주기 마다 수행,(배당금 중복 저장 방지가 필요함.)
	 	코드에 직접 주기를 입력해 놓는 것은 비효율
	 	스케줄 변경하고 싶을 때 마다 코드를 빌드하고 배포하는 과정을 거쳐야함
	 	서비스 중간에 변경 가능성이 있는 cron 스케쥴러 같은 경우 -> config 설정으로 관리
	*/
	@CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true) 	// finance 에 해당하는 데이터 모두 비움.
	@Scheduled(cron = "${scheduler.scrap.yahoo}")					// 회사 배당금 조회하는 시점에 데이터를 모두 먼저 비우고 다시 캐시에 데이터가 올라가게
	public void yahooFinanceScheduling() {
		log.info("scraping scheduler is started");
		// 저장된 회사 목록 조회
		List<CompanyEntity> companies = this.companyRepository.findAll();
			// -> 회사수 많아지면 데이터양 많아짐 크론을 사용해서 주기적으로 데이터 가져오기 버거울 수 있다
			// -> 스프링 배치 - 벌크 데이터 처리에 도움이 될 수 있음. (개선점)

		// 회사마다 배당금 정보를 새로 스크래핑
		for (var company : companies) {
//			log.info("scraping scheduler is started - > " + company.getName());
			ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(
					Company.builder()
							.name(company.getName())
							.ticker(company.getTicker())
							.build());
			// 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
			scrapedResult.getDividends().stream()
					//map 으로 디비든 모델은 디비든 엔티티로 매핑
					.map(e -> new DividendEntity(company.getId(), e))
					//엘리먼트를 하나씩 디비든 레파지토리에 삽입(조건에 따라)
					.forEach(e -> {
						boolean exists =
								this.dividendRepository.existsByCompanyIdAndDate(
										e.getCompanyId(), e.getDate());
						if (!exists) {
							this.dividendRepository.save(e);
							log.info("insert new dividend -> " + e.toString());
						}
					});

			// 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지(서버에 부하가 걸리지 않게)
			try {
				Thread.sleep(3000); // 3초
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
}
