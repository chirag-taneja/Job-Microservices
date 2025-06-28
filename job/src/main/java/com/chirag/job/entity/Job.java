package com.chirag.job.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToOne()
    @JoinColumn(name = "company_id",referencedColumnName = "id")
    @JsonBackReference
    private Company company;

   }
