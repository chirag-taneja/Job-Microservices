package com.chirag.job.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @JsonManagedReference
    @OneToMany(mappedBy = "company")
    private List<Job> jobList;

    @OneToMany(mappedBy = "company")
    @JsonManagedReference
    private List<Review> reviews;



}
