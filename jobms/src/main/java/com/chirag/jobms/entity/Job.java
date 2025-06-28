package com.chirag.jobms.entity;


import com.chirag.jobms.external.Company;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private String minSalary;
    private String maxSalary;

    private String location;

    private Long companyId;

    @Transient
    private Company company;


   }
