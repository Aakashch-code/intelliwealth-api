package com.example.intelliwealth.protection.insurance;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InsuranceService {

    private final InsuranceRepository repo;
    private final InsuranceMapper mapper;

    public InsuranceService(InsuranceRepository repo, InsuranceMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public InsuranceResponseDTO create(InsuranceRequestDTO dto) {
        InsuranceValidator.validateAttributes(
                dto.getCategory(), dto.getAttributes());

        Insurance insurance = mapper.toEntity(dto);
        return mapper.toDTO(repo.save(insurance));
    }

    public List<InsuranceResponseDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}
