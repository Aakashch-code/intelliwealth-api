package com.example.intelliwealth.service;

import com.example.intelliwealth.model.Asset;
import com.example.intelliwealth.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    @Autowired
    private AssetRepository repo;

    public List<Asset> getAllAssets() {
        return repo.findAll();
    }
}
