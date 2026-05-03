package com.example.intelliwealth.wealth.asset.infrastructure.persistence;

import com.example.intelliwealth.wealth.asset.application.dto.CategorySumResult;
import com.example.intelliwealth.wealth.asset.application.dto.MainCategorySumResult;
import com.example.intelliwealth.wealth.asset.domain.model.Asset;
import org.bson.types.Decimal128;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetRepository extends MongoRepository<Asset, String> {

    Page<Asset> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Asset> findByIdAndUserId(String id, UUID userId);

    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }",
            "{ $group: { _id: null, totalValue: { $sum: '$currentValue' } } }"
    })
    Decimal128 sumAssetValueByUserId(UUID userId);


    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }",
            "{ $group: { _id: '$category', total: { $sum: '$currentValue' } } }"
    })
    List<CategorySumResult> sumByCategory(UUID userId);

    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }",
            "{ $group: { _id: '$mainCategory', total: { $sum: '$currentValue' } } }"
    })
    List<MainCategorySumResult> sumByMainCategory(UUID userId);

    void deleteByIdAndUserId(String id, UUID userId);
}
