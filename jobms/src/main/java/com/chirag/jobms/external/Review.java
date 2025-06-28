package com.chirag.jobms.external;

import lombok.Data;

@Data
public class Review {
    private Long id;

    private String title;

    private String description;

    private Double rating;


    private Long companyId;
}
