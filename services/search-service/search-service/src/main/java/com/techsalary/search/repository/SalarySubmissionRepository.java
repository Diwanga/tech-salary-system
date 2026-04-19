package com.techsalary.search.repository;

import com.techsalary.search.model.SalarySubmission;
import com.techsalary.search.model.enums.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for salary submissions.
 * Extends JpaSpecificationExecutor to support dynamic multi-criteria queries.
 */
@Repository
public interface SalarySubmissionRepository extends JpaRepository<SalarySubmission, Long>,
        JpaSpecificationExecutor<SalarySubmission> {

    /**
     * Find a single approved submission by ID.
     */
    Optional<SalarySubmission> findByIdAndStatus(Long id, SubmissionStatus status);

    /**
     * Get distinct countries from approved submissions.
     */
    @Query("SELECT DISTINCT s.country FROM SalarySubmission s WHERE s.status = :status ORDER BY s.country")
    List<String> findDistinctCountriesByStatus(@Param("status") SubmissionStatus status);

    /**
     * Get distinct companies from approved submissions.
     */
    @Query("SELECT DISTINCT s.company FROM SalarySubmission s WHERE s.status = :status ORDER BY s.company")
    List<String> findDistinctCompaniesByStatus(@Param("status") SubmissionStatus status);

    /**
     * Get distinct job titles from approved submissions.
     */
    @Query("SELECT DISTINCT s.jobTitle FROM SalarySubmission s WHERE s.status = :status ORDER BY s.jobTitle")
    List<String> findDistinctJobTitlesByStatus(@Param("status") SubmissionStatus status);

    /**
     * Get distinct cities from approved submissions.
     */
    @Query("SELECT DISTINCT s.city FROM SalarySubmission s WHERE s.status = :status AND s.city IS NOT NULL ORDER BY s.city")
    List<String> findDistinctCitiesByStatus(@Param("status") SubmissionStatus status);

    /**
     * Get distinct technology names from approved submissions.
     */
    @Query("SELECT DISTINCT t.name FROM SalarySubmission s JOIN s.technologies t WHERE s.status = :status ORDER BY t.name")
    List<String> findDistinctTechnologiesByStatus(@Param("status") SubmissionStatus status);
}
