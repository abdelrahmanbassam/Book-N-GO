package com.example.book_n_go.service;

import com.example.book_n_go.dto.HallsFilterRequest;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.repository.HallRepo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HallsListFilterServiceTest {

    @Mock
    private HallRepo hallRepo;

    @InjectMocks
    private HallsListFilterService hallsListFilterService;

    private HallsFilterRequest filterRequest;

    @BeforeEach
    public void setUp() {
        filterRequest = new HallsFilterRequest();
        filterRequest.setPage(1);
        filterRequest.setPageSize(5);
        filterRequest.setRating(4.0);
        filterRequest.setSearchWord("Luxury");
        filterRequest.setAminities(List.of("WiFi", "Parking"));
        filterRequest.setSortBy("name");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testApplyCriterias_FullFilter() {
        Hall hall = new Hall();
        hall.setId(1L);
        hall.setName("Luxury Hall");
        hall.setRating(4.5);

        Page<Hall> expectedPage = new PageImpl<>(List.of(hall), PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "name")), 1);

        when(hallRepo.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(expectedPage);

        Page<Hall> result = hallsListFilterService.applyCriterias(filterRequest);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals("Luxury Hall", result.getContent().get(0).getName());
    }


    @Test
    public void testBuildSpecification_NoFilters() {
        filterRequest.setRating(null);
        filterRequest.setSearchWord(null);
        filterRequest.setAminities(null);

        Specification<Hall> specification = hallsListFilterService.buildSpecification(filterRequest);

        Predicate predicate = specification.toPredicate(mockRoot(), mockQuery(), mockCriteriaBuilder());
        assertNull(predicate, "Predicate should be null for no filters");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testApplyCriterias_NoMatchingHalls() {
        Page<Hall> expectedPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 5), 0);

        when(hallRepo.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(expectedPage);

        Page<Hall> result = hallsListFilterService.applyCriterias(filterRequest);

        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }


    @SuppressWarnings("unchecked")
    private Root<Hall> mockRoot() {
        return mock(Root.class);
    }

    private CriteriaQuery<?> mockQuery() {
        return mock(CriteriaQuery.class);
    }

    private CriteriaBuilder mockCriteriaBuilder() {
        return mock(CriteriaBuilder.class);
    }
}
