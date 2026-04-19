package com.techsalary.search.service;

import com.techsalary.search.config.AppConfig;
import com.techsalary.search.dto.FilterOptionsResponse;
import com.techsalary.search.dto.SalarySearchRequest;
import com.techsalary.search.dto.SalarySearchResponse;
import com.techsalary.search.dto.SalarySearchResponse.SalarySearchResult;
import com.techsalary.search.model.SalarySubmission;
import com.techsalary.search.model.Technology;
import com.techsalary.search.model.enums.EmploymentType;
import com.techsalary.search.model.enums.ExperienceLevel;
import com.techsalary.search.model.enums.RemoteType;
import com.techsalary.search.model.enums.SubmissionStatus;
import com.techsalary.search.repository.SalarySubmissionRepository;
import com.techsalary.search.specification.SalarySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for salary search operations.
 * Handles query building, pagination, sorting, and response mapping.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SearchService {

    private final SalarySubmissionRepository repository;
    private final AppConfig appConfig;

    /**
     * Search salary submissions with dynamic filtering,
     * sorting, and pagination.
     */
    public SalarySearchResponse searchSalaries(SalarySearchRequest request) {
        // Validate and cap page size
        int pageSize = Math.min(
                request.getSize() > 0 ? request.getSize() : appConfig.getSearch().getDefaultPageSize(),
                appConfig.getSearch().getMaxPageSize()
        );
        int pageNumber = Math.max(request.getPage(), 0);

        // Build sort
        Sort sort = buildSort(request.getSortBy(), request.getSortDir());

        // Build pageable
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Build specification from filters
        Specification<SalarySubmission> spec = SalarySpecification.withFilters(request);

        // Execute query
        log.debug("Executing search with filters: country={}, company={}, jobTitle={}, level={}, page={}, size={}",
                request.getCountry(), request.getCompany(), request.getJobTitle(),
                request.getExperienceLevel(), pageNumber, pageSize);

        Page<SalarySubmission> page = repository.findAll(spec, pageable);

        // Map to response
        List<SalarySearchResult> results = page.getContent().stream()
                .map(this::mapToSearchResult)
                .collect(Collectors.toList());

        return SalarySearchResponse.builder()
                .results(results)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    /**
     * Get a single approved salary submission by ID.
     */
    public Optional<SalarySearchResult> getSalaryById(Long id) {
        log.debug("Fetching salary submission by ID: {}", id);
        return repository.findByIdAndStatus(id, SubmissionStatus.APPROVED)
                .map(this::mapToSearchResult);
    }

    /**
     * Get available filter options from approved submissions.
     * Used by the frontend to populate dropdown menus.
     */
    public FilterOptionsResponse getFilterOptions() {
        log.debug("Fetching filter options for approved submissions");

        return FilterOptionsResponse.builder()
                .countries(repository.findDistinctCountriesByStatus(SubmissionStatus.APPROVED))
                .companies(repository.findDistinctCompaniesByStatus(SubmissionStatus.APPROVED))
                .jobTitles(repository.findDistinctJobTitlesByStatus(SubmissionStatus.APPROVED))
                .cities(repository.findDistinctCitiesByStatus(SubmissionStatus.APPROVED))
                .technologies(repository.findDistinctTechnologiesByStatus(SubmissionStatus.APPROVED))
                .experienceLevels(
                        Arrays.stream(ExperienceLevel.values())
                                .map(Enum::name)
                                .collect(Collectors.toList())
                )
                .employmentTypes(
                        Arrays.stream(EmploymentType.values())
                                .map(Enum::name)
                                .collect(Collectors.toList())
                )
                .remoteTypes(
                        Arrays.stream(RemoteType.values())
                                .map(Enum::name)
                                .collect(Collectors.toList())
                )
                .build();
    }

    // ======================== Private Helpers ========================

    /**
     * Map JPA entity to API response DTO.
     */
    private SalarySearchResult mapToSearchResult(SalarySubmission submission) {
        List<String> techNames = submission.getTechnologies().stream()
                .map(Technology::getName)
                .sorted()
                .collect(Collectors.toList());

        return SalarySearchResult.builder()
                .id(submission.getId())
                .jobTitle(submission.getJobTitle())
                .company(submission.getCompany())
                .country(submission.getCountry())
                .city(submission.getCity())
                .experienceLevel(submission.getExperienceLevel().name())
                .yearsOfExperience(submission.getYearsOfExperience())
                .grossMonthlySalary(submission.getGrossMonthlySalary())
                .netMonthlySalary(submission.getNetMonthlySalary())
                .currency(submission.getCurrency())
                .employmentType(submission.getEmploymentType().name())
                .remoteType(submission.getRemoteType().name())
                .technologies(techNames)
                .additionalNotes(submission.getAdditionalNotes())
                .upvoteCount(submission.getUpvoteCount())
                .downvoteCount(submission.getDownvoteCount())
                .voteCount(submission.getVoteCount())
                .submittedAt(submission.getSubmittedAt())
                .build();
    }

    /**
     * Build Sort object from sortBy and sortDir parameters.
     */
    private Sort buildSort(String sortBy, String sortDir) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        String sortField = switch (sortBy != null ? sortBy.toLowerCase() : "date") {
            case "salary" -> "grossMonthlySalary";
            case "votes" -> "voteCount";
            case "date" -> "submittedAt";
            case "company" -> "company";
            case "title" -> "jobTitle";
            default -> "submittedAt";
        };

        return Sort.by(direction, sortField);
    }
}
