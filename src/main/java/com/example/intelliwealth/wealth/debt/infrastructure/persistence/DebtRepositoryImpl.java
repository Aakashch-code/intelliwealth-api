package com.example.intelliwealth.wealth.debt.infrastructure.persistence;

import com.example.intelliwealth.wealth.debt.domain.exception.DebtNotFoundException;
import com.example.intelliwealth.wealth.debt.domain.model.Debt;
import com.example.intelliwealth.wealth.debt.domain.repository.DebtRepository; // Import the Domain Interface
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component // Or @Repository
@RequiredArgsConstructor
public class DebtRepositoryImpl implements DebtRepository {

    // Inject the Spring Magic Repository
    private final SpringDataDebtRepository springDataRepo;

    @Override
    public Debt save(Debt debt) {
        return springDataRepo.save(debt);
    }

    @Override
    public List<Debt> findAllByUserId(UUID userId) {
        return springDataRepo.findAllByUserId(userId);
    }

    @Override
    public Optional<Debt> findById(String id) {
        return springDataRepo.findById(id);
    }
    @Override
    public void deleteByIdAndUserId(String id, UUID userId) {
        long deleted = springDataRepo.deleteByIdAndUserId(id, userId);

        if (deleted == 0) {
            throw new DebtNotFoundException(id);
        }
    }
}