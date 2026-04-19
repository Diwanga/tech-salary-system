package com.techsalary.search.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Application-level configuration properties.
 * All values are configurable via application.yml or environment variables.
 */
@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfig {

    /** Default currency for salary display (default: LKR) */
    private String defaultCurrency = "LKR";

    /** Number of net upvotes required for auto-approval (default: 5) */
    private int voteThreshold = 5;

    /** Search-specific configuration */
    private SearchConfig search = new SearchConfig();

    @Getter
    @Setter
    public static class SearchConfig {
        /** Default page size for search results (default: 20) */
        private int defaultPageSize = 20;

        /** Maximum allowed page size (default: 100) */
        private int maxPageSize = 100;
    }
}
