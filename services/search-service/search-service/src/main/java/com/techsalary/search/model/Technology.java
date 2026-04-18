package com.techsalary.search.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * JPA entity for the technologies lookup table.
 * Used in many-to-many relationship with SalarySubmission.
 */
@Entity
@Table(name = "technologies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @ManyToMany(mappedBy = "technologies")
    @Builder.Default
    private Set<SalarySubmission> submissions = new HashSet<>();

    @Override
    public String toString() {
        return name;
    }
}
