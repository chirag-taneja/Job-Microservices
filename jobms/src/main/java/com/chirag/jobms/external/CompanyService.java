package com.chirag.jobms.external;

import jakarta.ws.rs.Path;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "COMPANY-MS")
public interface CompanyService {

    @GetMapping("/company/{id}")
    Company getCompany(@PathVariable Long id);
}
