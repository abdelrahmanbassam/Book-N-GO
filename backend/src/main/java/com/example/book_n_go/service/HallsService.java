package com.example.book_n_go.service;

import com.example.book_n_go.dto.HallRequest;
import com.example.book_n_go.model.Aminity;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.model.Workspace;
import com.example.book_n_go.repository.AminityRepo;
import com.example.book_n_go.repository.HallRepo;
import com.example.book_n_go.repository.WorkspaceRepo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class HallsService {

  @Autowired
  private HallRepo hallRepo;
  @Autowired
  private WorkspaceRepo workspaceRepo;
  @Autowired
  private AminityRepo aminityRepo;

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
