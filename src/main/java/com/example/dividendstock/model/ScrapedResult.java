package com.example.dividendstock.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ScrapedResult {    // 스크랩핑한 결과 주고받기 위한 클래스

	private Company company;
	private List<Dividend> dividends;

	public ScrapedResult() {
		this.dividends = new ArrayList<>();
	}
}
