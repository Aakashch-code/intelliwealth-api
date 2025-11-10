package com.example.intelliwealth.controller;

import com.example.intelliwealth.model.Asset;
import com.example.intelliwealth.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AssetController {

    @Autowired
    private AssetService service;

    @GetMapping("/assets")
    private List<Asset> getAllAssets() {
        return service.getAllAssets();
    }

}
