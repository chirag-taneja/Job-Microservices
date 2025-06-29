package com.chirag.jobms.external;

import lombok.Data;

import java.util.List;

@Data
public class Company {
    private Long id;

    private String name;

    private String description;

    List<Review> reviews;
}
