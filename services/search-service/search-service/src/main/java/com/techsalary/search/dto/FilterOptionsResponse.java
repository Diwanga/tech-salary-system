package com.techsalary.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for available filter options.
 * Used by the frontend to populate dropdown menus.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterOptionsResponse {

    /** Distinct countries available in approved submissions */
    private List<String> countries;

    /** Distinct companies available in approved submissions */
    private List<String> companies;

    /** Distinct job titles available in approved submissions */
    private List<String> jobTitles;

    /** Distinct cities available in approved submissions */
    private List<String> cities;

    /** Available experience levels */
    private List<String> experienceLevels;

    /** Available employment types */
    private List<String> employmentTypes;

    /** Available remote types */
    private List<String> remoteTypes;

    /** Distinct technologies available in approved submissions */
    private List<String> technologies;
}
