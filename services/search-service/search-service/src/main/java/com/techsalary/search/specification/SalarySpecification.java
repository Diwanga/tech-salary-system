package com.techsalary.search.specification;

import com.techsalary.search.dto.SalarySearchRequest;
import com.techsalary.search.model.SalarySubmission;
import com.techsalary.search.model.Technology;
import com.techsalary.search.model.enums.SubmissionStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specification for building dynamic search queries.
 * Supports multi-criteria filtering with optional parameters.
 * Only returns APPROVED submissions.
 */
public class SalarySpecification {

    private SalarySpecification() {
        // Utility class — prevent instantiation
    }

    /**
     * Build a Specification from the search request.
     * Null/empty filters are ignored, allowing flexible combinations.
     */
    public static Specification<SalarySubmission> withFilters(SalarySearchRequest request) {
        return (Root<SalarySubmission> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // ---- MANDATORY: Only return APPROVED submissions ----
            predicates.add(cb.equal(root.get("status"), SubmissionStatus.APPROVED));

            // ---- Country filter (case-insensitive exact match) ----
            if (request.getCountry() != null && !request.getCountry().isBlank()) {
                predicates.add(cb.equal(
                        cb.lower(root.get("country")),
                        request.getCountry().toLowerCase().trim()
                ));
            }

            // ---- Company filter (case-insensitive partial match) ----
            if (request.getCompany() != null && !request.getCompany().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("company")),
                        "%" + request.getCompany().toLowerCase().trim() + "%"
                ));
            }

            // ---- Job title filter (case-insensitive partial match) ----
            if (request.getJobTitle() != null && !request.getJobTitle().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("jobTitle")),
                        "%" + request.getJobTitle().toLowerCase().trim() + "%"
                ));
            }

            // ---- City filter (case-insensitive partial match) ----
            if (request.getCity() != null && !request.getCity().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("city")),
                        "%" + request.getCity().toLowerCase().trim() + "%"
                ));
            }

            // ---- Experience level filter (exact match) ----
            if (request.getExperienceLevel() != null) {
                predicates.add(cb.equal(root.get("experienceLevel"), request.getExperienceLevel()));
            }

            // ---- Employment type filter (exact match) ----
            if (request.getEmploymentType() != null) {
                predicates.add(cb.equal(root.get("employmentType"), request.getEmploymentType()));
            }

            // ---- Remote type filter (exact match) ----
            if (request.getRemoteType() != null) {
                predicates.add(cb.equal(root.get("remoteType"), request.getRemoteType()));
            }

            // ---- Salary range filters ----
            if (request.getMinSalary() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("grossMonthlySalary"), request.getMinSalary()
                ));
            }
            if (request.getMaxSalary() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("grossMonthlySalary"), request.getMaxSalary()
                ));
            }

            // ---- Technology filter (joins technologies table) ----
            if (request.getTechStack() != null && !request.getTechStack().isBlank()) {
                Join<SalarySubmission, Technology> techJoin = root.join("technologies", JoinType.INNER);
                predicates.add(cb.like(
                        cb.lower(techJoin.get("name")),
                        "%" + request.getTechStack().toLowerCase().trim() + "%"
                ));
                // Prevent duplicate results from the join
                query.distinct(true);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
