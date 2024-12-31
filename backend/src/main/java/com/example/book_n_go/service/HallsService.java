package com.example.book_n_go.service;

import com.example.book_n_go.dto.HallRequest;
import com.example.book_n_go.dto.HallsFilterRequest;
import com.example.book_n_go.model.Aminity;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.model.Workspace;
import com.example.book_n_go.repository.AminityRepo;
import com.example.book_n_go.repository.HallRepo;
import com.example.book_n_go.repository.WorkspaceRepo;

import jakarta.persistence.criteria.Join;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HallsService {

  @Autowired
  private HallRepo hallRepo;
  @Autowired
  private WorkspaceRepo workspaceRepo;
  @Autowired
  private AminityRepo aminityRepo;

  public List<Hall> applyCriterias(HallsFilterRequest request) {
    Specification<Hall> spec = buildSpecification(request);

    Sort sort = "none".equals(request.getSortBy()) ? Sort.by(Sort.Direction.DESC, "id")
        : Sort.by(Sort.Direction.DESC, request.getSortBy());

    return hallRepo.findAll(spec, sort);
    // return hallRepo.findAll(spec);
  }

  private Specification<Hall> buildSpecification(HallsFilterRequest request) {
    return (root, query, criteriaBuilder) -> {
      Specification<Hall> spec = Specification.where(null);

      // // Filter by rating
      if (request.getRating() != null) {
        spec = spec.and((hall, query1, cB) -> cB.greaterThanOrEqualTo(hall.get("rating"), request.getRating()));
      }

      // Search by keyword in name
      if (request.getSearchWord() != null && !request.getSearchWord().isEmpty()) {
        spec = spec.and((hall, query1, cB) -> cB.like(cB.lower(hall.get("name")),
            "%" + request.getSearchWord().toLowerCase() + "%"));
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

  public Hall createHall(HallRequest hallRequest, Long workspaceId) {
    Workspace workspace = workspaceRepo.findById(workspaceId).get();

    Hall hall = new Hall();
    hall.setName(hallRequest.getName());
    hall.setCapacity(hallRequest.getCapacity());
    hall.setDescription(hallRequest.getDescription());
    hall.setPricePerHour(hallRequest.getPricePerHour());
    hall.setRating(0);

    Set<Aminity> aminities = new HashSet<>();
    for(Long aminityId: hallRequest.getAminitiesIds()) {
        aminities.add(aminityRepo.findById(aminityId).get());
    }
    hall.setAminities(aminities);
    hall.setWorkspace(workspace);
    Hall _hall = hallRepo.save(hall);
    return _hall;
  }

}
