package com.example.intelliwealth.fynix.infrastructure.persistence;

import com.example.intelliwealth.fynix.application.dto.FynixChatRequest;
import com.example.intelliwealth.fynix.domain.model.FynixHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FynixHistoryRepository extends MongoRepository<FynixHistory, String> {

    List<FynixHistory> findByUserIdOrderByCreatedAtDesc(String userId);

    List<FynixHistory> findByUserIdAndConversationIdOrderByCreatedAtAsc(
            String userId,
            String conversationId
    );
    @Query("""
SELECT DISTINCT f.conversationId,
       ,MIN(f.createdAt), 
       MIN(f.query)
FROM FynixHistory f
WHERE f.userId = :userId
GROUP BY f.conversationId
ORDER BY MIN(f.createdAt) DESC
""")
    List<Object[]> findConversations(String userId);

}