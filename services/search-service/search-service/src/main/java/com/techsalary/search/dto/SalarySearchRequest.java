package com.techsalary.search.dto;

import com.techsalary.search.model.enums.EmploymentType;
import com.techsalary.search.model.enums.ExperienceLevel;
import com.techsalary.search.model.enums.RemoteType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Request DTO for search query parameters.
 * All fields are optional — when null, that filter is not applied.
 */
@Data
public class SalarySearchRequest {

    /** Filter by country (exact match, case-insensitive) */
    private String country;

    /** Filter by company (partial match, case-insensitive) */
    private String company;

    /** Filter by job title (partial match, case-insensitive) */
    private String jobTitle;

    /** Filter by experience level (exact match) */
    private ExperienceLevel experienceLevel;

    /** Filter by employment type (exact match) */
    private EmploymentType employmentType;

    /** Filter by remote work type (exact match) */
    private RemoteType remoteType;

    /** Filter by minimum gross monthly salary */
    private BigDecimal minSalary;

    /** Filter by maximum gross monthly salary */
    private BigDecimal maxSalary;

    /** Filter by technology/tech stack (partial match, case-insensitive) */
    private String techStack;

    /** Filter by city (partial match, case-insensitive) */
    private String city;

    /** Sort field: "salary", "date", "votes" (default: "date") */
    private String sortBy = "date";

    /** Sort direction: "asc" or "desc" (default: "desc") */
    private String sortDir = "desc";

    /** Page number, 0-indexed (default: 0) */
    private int page = 0;

    /** Page size (default: 20, max: 100) */
    private int size = 20;
}
