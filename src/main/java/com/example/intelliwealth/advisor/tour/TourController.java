package com.example.intelliwealth.advisor.tour;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tour")
public class TourController {

    @Autowired
    private TourService service;

    @GetMapping
    public TourSummaryDTO getTourDetails() {
        return service.getTourSummary();
    }
}
