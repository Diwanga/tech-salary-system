package com.techsalary.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for search results with pagination metadata.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalarySearchResponse {

    /** List of matching salary records */
    private List<SalarySearchResult> results;

    /** Current page number (0-indexed) */
    private int page;

    /** Page size */
    private int size;

    /** Total number of matching records */
    private long totalElements;

    /** Total number of pages */
    private int totalPages;

    /** Whether there is a next page */
    private boolean hasNext;

    /** Whether there is a previous page */
    private boolean hasPrevious;

    /**
     * Individual salary search result.
     * Flattened from the SalarySubmission entity for API response.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalarySearchResult {

        private Long id;
        private String jobTitle;
        private String company;
        private String country;
        private String city;
        private String experienceLevel;
        private int yearsOfExperience;
        private BigDecimal grossMonthlySalary;
        private BigDecimal netMonthlySalary;
        private String currency;
        private String employmentType;
        private String remoteType;
        private List<String> technologies;
        private String additionalNotes;
        private int upvoteCount;
        private int downvoteCount;
        private int voteCount;
        private LocalDateTime submittedAt;
    }
}
