package com.levelup.recruit.domain.entity;

import com.levelup.recruit.domain.entity.enumeration.ClosingType;
import com.levelup.recruit.domain.entity.enumeration.Company;
import com.levelup.recruit.domain.entity.enumeration.JobStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "job")
@Entity
public class Job {

    @Id @GeneratedValue
    @Column(name = "job_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Company company;

    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus jobStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClosingType closingType;

    private LocalDateTime openDate;

    private LocalDateTime closingDate;

    public static Job of(
            String title,
            Company company,
            String url,
            JobStatus jobStatus,
            ClosingType closingType,
            LocalDateTime openDate,
            LocalDateTime closingDate)
    {
        return new Job(null, title, company, url, jobStatus, closingType, openDate, closingDate);
    }

    public static Job of(
            String title,
            Company company,
            String url,
            JobStatus jobStatus,
            LocalDateTime openDate)
    {
        return new Job(
                null,
                title,
                company,
                url,
                jobStatus,
                ClosingType.INFINITE,
                openDate,
                LocalDateTime.of(2099, 12, 12, 0, 0));
    }
}
