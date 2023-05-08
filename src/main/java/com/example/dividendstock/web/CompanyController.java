package com.example.dividendstock.web;

import com.example.dividendstock.model.Company;
import com.example.dividendstock.model.constants.CacheKey;
import com.example.dividendstock.persist.entity.CompanyEntity;
import com.example.dividendstock.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

	private final CompanyService companyService;

	private final CacheManager redisCacheManager;

	@GetMapping("/autocomplete")
	public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
		return ResponseEntity.ok(companyService.getCompanyNamesByKeyword(keyword));
	}

	@GetMapping
	@PreAuthorize("hasRole('READ')")
	public ResponseEntity<?> searchCompany(final Pageable pageable) {
		Page<CompanyEntity> companies = this.companyService.getAllCompany(pageable);
		return ResponseEntity.ok(companies);
	}

	/**
	 * 회사 및 배당금 정보 추가
	 *
	 */
	@PostMapping
	@PreAuthorize("hasRole('WRITE')")	// 쓰기 권한 있는 유저만 호출 가능
	public ResponseEntity<?> addCompany(@RequestBody Company request) {
		String ticker = request.getTicker().trim();
		if (ObjectUtils.isEmpty(ticker)) {
			throw new RuntimeException("ticker is empty");
		}

		Company company = this.companyService.save(ticker);
		this.companyService.addAutocompleteKeyword(company.getName());
		return ResponseEntity.ok(company);
	}

	@DeleteMapping("/{ticker}")
	@PreAuthorize("hasRole('WRITE')")
	public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
		String companyName = this.companyService.deleteCompany(ticker);
		this.clearFinanceCache(companyName);
		return ResponseEntity.ok(companyName);
	}

	public void clearFinanceCache(String companyName) {
		this.redisCacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);
	}

}
