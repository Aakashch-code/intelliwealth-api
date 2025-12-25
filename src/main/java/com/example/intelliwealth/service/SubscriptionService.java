package com.example.intelliwealth.service;

import com.example.intelliwealth.dto.subscription.SubscriptionRequestDTO;
import com.example.intelliwealth.dto.subscription.SubscriptionResponseDTO;
import com.example.intelliwealth.mapper.subscription.SubscriptionMapper;
import com.example.intelliwealth.model.Subscription;
import com.example.intelliwealth.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository repo;
    private final SubscriptionMapper mapper;

    // --- Read Operations ---

    public List<SubscriptionResponseDTO> getAllSubscriptions() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    public List<SubscriptionResponseDTO> getActiveSubscriptions() {
        return repo.findByIsActiveTrue().stream().map(mapper::toResponse).toList();
    }

    public List<SubscriptionResponseDTO> getInactiveSubscriptions() {
        return repo.findByIsActiveFalse().stream().map(mapper::toResponse).toList();
    }

    public SubscriptionResponseDTO getSubscriptionById(Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + id));
    }

    // --- Write Operations ---

    public SubscriptionResponseDTO createSubscription(SubscriptionRequestDTO dto) {
        Subscription saved = repo.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    @Transactional
    public SubscriptionResponseDTO toggleSubscriptionStatus(Long id) {
        Subscription sub = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + id));

        sub.setActive(!sub.isActive());
        return mapper.toResponse(sub);
    }

    public void hardDeleteSubscription(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Subscription not found: " + id);
        }
        repo.deleteById(id);
    }
}