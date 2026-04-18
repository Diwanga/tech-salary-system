package com.techsalary.search.controller;

import com.techsalary.search.dto.FilterOptionsResponse;
import com.techsalary.search.dto.SalarySearchRequest;
import com.techsalary.search.dto.SalarySearchResponse;
import com.techsalary.search.dto.SalarySearchResponse.SalarySearchResult;
import com.techsalary.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for salary search endpoints.
 * All endpoints are public — no authentication required for searching.
 *
 * Endpoints:
 *   GET /api/search           - Search salaries with filters
 *   GET /api/search/{id}      - Get single salary by ID
 *   GET /api/search/filters   - Get available filter options
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final SearchService searchService;

    /**
     * Search salary submissions with optional filters.
     *
     * Query Parameters:
     *   country, company, jobTitle, city,
     *   experienceLevel (JUNIOR|MID|SENIOR|LEAD|PRINCIPAL),
     *   employmentType (FULL_TIME|PART_TIME|CONTRACT|FREELANCE),
     *   remoteType (ONSITE|HYBRID|REMOTE),
     *   minSalary, maxSalary,
     *   techStack,
     *   sortBy (salary|date|votes|company|title),
     *   sortDir (asc|desc),
     *   page (0-indexed), size
     *
     * @return Paginated search results with only APPROVED submissions
     */
    @GetMapping
    public ResponseEntity<SalarySearchResponse> searchSalaries(
            @ModelAttribute SalarySearchRequest request) {
        log.info("Search request received: country={}, company={}, jobTitle={}, level={}, page={}, size={}",
                request.getCountry(), request.getCompany(), request.getJobTitle(),
                request.getExperienceLevel(), request.getPage(), request.getSize());

        SalarySearchResponse response = searchService.searchSalaries(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a single approved salary submission by ID.
     *
     * @param id The submission ID
     * @return The salary record if found and APPROVED, 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<SalarySearchResult> getSalaryById(@PathVariable Long id) {
        log.info("Get salary by ID: {}", id);

        return searchService.getSalaryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get available filter options for the search UI.
     * Returns distinct values for countries, companies, job titles,
     * cities, and technologies from approved submissions.
     *
     * @return Filter options for populating dropdown menus
     */
    @GetMapping("/filters")
    public ResponseEntity<FilterOptionsResponse> getFilterOptions() {
        log.info("Filter options request received");

        FilterOptionsResponse response = searchService.getFilterOptions();
        return ResponseEntity.ok(response);
    }
}
