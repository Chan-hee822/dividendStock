package com.example.dividendstock.scraper;

import com.example.dividendstock.model.Company;
import com.example.dividendstock.model.ScrapedResult;

public interface Scraper {        // 야후 파이낸스 말고 네이버 등 여러 곳에서 스크랩 해오기 쉽게 범용성 키움.
	Company scrapCompanyByTicker(String ticker);

	ScrapedResult scrap(Company company);
}
