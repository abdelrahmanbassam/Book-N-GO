package com.example.book_n_go.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.book_n_go.dto.HallRequest;
import com.example.book_n_go.dto.HallsFilterRequest;
import com.example.book_n_go.enums.Permission;
import com.example.book_n_go.model.Aminity;
import com.example.book_n_go.model.Hall;
import com.example.book_n_go.model.Workspace;
import com.example.book_n_go.repository.AminityRepo;
import com.example.book_n_go.repository.HallRepo;
import com.example.book_n_go.repository.WorkspaceRepo;

import com.example.book_n_go.service.HallsListFilterService;
import com.example.book_n_go.service.AuthService;
import com.example.book_n_go.service.HallsService;

@RestController
@RequestMapping("/workspace/{workspaceId}")
@CrossOrigin(origins = "http://localhost:3000")
public class HallController {
    @Autowired
    private HallRepo hallRepo;
    @Autowired
    private WorkspaceRepo workspaceRepo;
    @Autowired
    private AminityRepo aminityRepo;

    @GetMapping("/halls")
    public ResponseEntity<List<Hall>> getHalls(@PathVariable("workspaceId") long workspaceId) {
        try {
            System.out.println("Workspace ID: " + workspaceId);
            List<Hall> halls = new ArrayList<Hall>();
            Workspace workspace = workspaceRepo.findById(workspaceId).get();
            hallRepo.findByWorkspace(workspace).forEach(halls::add);
            if (halls.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(halls, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/halls/{id}")
    public ResponseEntity<Hall> getHallById(@PathVariable("id") long id) {
        Optional<Hall> hallData = hallRepo.findById(id);
        if (hallData.isPresent()) {
            return new ResponseEntity<>(hallData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/halls")
    public ResponseEntity<Hall> createHall(@RequestBody HallRequest hallRequest, @PathVariable("workspaceId") long workspaceId) {
        Workspace workspace = workspaceRepo.findById(workspaceId).get();
        if (!workspace.getProvider().getId().equals(AuthService.getRequestUser().getId()) || !AuthService.userHasPermission(Permission.PROVIDER_WRITE)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(workspace == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // if (workspace.getProvider().getId() != AuthService.getRequestUser().getId()) {
        //     return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // }
        Hall _hall = hallsService.createHall(hallRequest, workspaceId);
        return new ResponseEntity<>(_hall, HttpStatus.CREATED);
    }

    @PutMapping("/halls/{id}")
    public ResponseEntity<Hall> updateHall(@PathVariable("id") long id, @RequestBody HallRequest hall) {
        Optional<Hall> hallData = hallRepo.findById(id);
        if (!AuthService.userHasPermission(Permission.PROVIDER_UPDATE) || !AuthService.getRequestUser().getId().equals(hallData.get().getWorkspace().getProvider().getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (hallData.isPresent()) {
            Hall _hall = hallData.get();
            _hall.setCapacity(hall.getCapacity());
            _hall.setDescription(hall.getDescription());
            _hall.setPricePerHour(hall.getPricePerHour());
            Set<Aminity> aminities = new HashSet<>();
            for(Long aminityId: hall.getAminitiesIds()) {
                aminities.add(aminityRepo.findById(aminityId).get());
            }
            _hall.setAminities(aminities);
            return new ResponseEntity<>(hallRepo.save(_hall), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/halls/{id}")
    public ResponseEntity<HttpStatus> deleteHall(@PathVariable("id") long id) {
        try {
            if(!AuthService.userHasPermission(Permission.PROVIDER_DELETE) || !AuthService.getRequestUser().getId().equals(hallRepo.findById(id).get().getWorkspace().getProvider().getId())) {
              return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            hallRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    private HallsService hallsService;
    @PostMapping("/filterHalls")
    public ResponseEntity<Map<String, Object>> filterHalls(@RequestBody HallsFilterRequest request) {
        try {
            Page<Hall> pageHalls = hallsListFilterService.applyCriterias(request);
            List<Hall> halls = pageHalls.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("halls", halls);
            Map<String, Object> pagination = new HashMap<>();
            pagination.put("currentPage", pageHalls.getNumber() + 1);
            pagination.put("totalPages", pageHalls.getTotalPages());
            pagination.put("pageSize", pageHalls.getSize());
            pagination.put("totalItems", pageHalls.getTotalElements());
            response.put("pagination", pagination);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}