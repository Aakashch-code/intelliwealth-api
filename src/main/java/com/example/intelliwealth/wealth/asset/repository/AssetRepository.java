package com.example.intelliwealth.wealth.asset.repository;

import com.example.intelliwealth.wealth.asset.domain.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends MongoRepository<Asset, String> {

}