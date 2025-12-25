package com.example.intelliwealth.controller;


import com.example.intelliwealth.service.NetWorthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/networth")
public class NetWorthController {

    @Autowired
    private NetWorthService service;

    @GetMapping("")
    public BigDecimal getNetWorth() {
        return service.calculateNetWorth();
    }


}
