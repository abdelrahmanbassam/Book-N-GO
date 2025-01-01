package com.example.book_n_go.service;

import com.example.book_n_go.dto.HallsFilterRequest;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.repository.HallRepo;

import jakarta.persistence.criteria.Join;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;



@Service
public class HallsListFilterService {

    @Autowired
    private HallRepo hallRepo;

    public Page<Hall> applyCriterias(HallsFilterRequest request) {
        Specification<Hall> spec = buildSpecification(request);

        Sort sort = "none".equals(request.getSortBy()) ? Sort.by(Sort.Direction.DESC, "id") : Sort.by(Sort.Direction.DESC, request.getSortBy());
        PageRequest pageable = PageRequest.of(request.getPage() - 1, request.getPageSize(), sort);

        return hallRepo.findAll(spec, pageable);
    }

    Specification<Hall> buildSpecification(HallsFilterRequest request) {
        return (root, query, criteriaBuilder) -> {
            Specification<Hall> spec = Specification.where(null);

            // // Filter by rating
            if (request.getRating() != null) {
                spec = spec.and((hall, query1, cB) -> 
                    cB.greaterThanOrEqualTo(hall.get("rating"), request.getRating()));
            }

            // Search by keyword in name
            if (request.getSearchWord() != null && !request.getSearchWord().isEmpty()) {
                spec = spec.and((hall, query1, cB) ->
                    cB.like(cB.lower(hall.get("name")), "%" + request.getSearchWord().toLowerCase() + "%"));
            }

            // Filter by Aminities
            if (request.getAminities() != null && !request.getAminities().isEmpty()) {
                for (String aminity : request.getAminities()) {
                    spec = spec.and((hall, query1, cB) -> {
                        Join<Object, Object> aminities = hall.join("aminities");
                        return cB.equal(aminities.get("name"), aminity);
                    });
                }
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }
}
