package com.techsalary.search.model.enums;

/**
 * Status of a salary submission in its lifecycle.
 * PENDING  -> waiting for community votes
 * APPROVED -> reached vote threshold, visible in search
 * REJECTED -> flagged/reported, hidden from search
 */
public enum SubmissionStatus {
    PENDING,
    APPROVED,
    REJECTED
}
