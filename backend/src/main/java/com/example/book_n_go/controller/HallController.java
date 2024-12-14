package com.example.book_n_go.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.book_n_go.model.Hall;
import com.example.book_n_go.repository.HallRepo;

@RestController
// @PreAuthorize("hasAnyRole('Role.ADMIN.name()', 'Role.CLIENT.name()', 'Role.PROVIDER.name()')")
public class HallController {
    @Autowired
    private HallRepo hallRepo;

    @GetMapping("/halls")
    // @PreAuthorize("hasAnyAuthority('client:read', 'provider:read', 'admin:read')")
    public ResponseEntity<List<Hall>> getHalls() {
        try {
            List<Hall> halls = new ArrayList<Hall>();
            hallRepo.findAll().forEach(halls::add);
            if (halls.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(halls, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/halls/{id}")
    // @PreAuthorize("hasAnyAuthority('client:read', 'provider:read', 'admin:read')")
    public ResponseEntity<Hall> getHallById(@PathVariable("id") long id) {
        Optional<Hall> hallData = hallRepo.findById(id);
        if (hallData.isPresent()) {
            return new ResponseEntity<>(hallData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/halls")
    // @PreAuthorize("hasAnyAuthority('provider:write', 'admin:write')")
    public ResponseEntity<Hall> createHall(@RequestBody Hall hall) {
        try {
            Hall _hall = hallRepo.save(hall);
            return new ResponseEntity<>(_hall, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/halls/{id}")
    // @PreAuthorize("hasAnyAuthority('provider:update', 'admin:update')")
    public ResponseEntity<Hall> updateHall(@PathVariable("id") long id, @RequestBody Hall hall) {
        Optional<Hall> hallData = hallRepo.findById(id);
        if (hallData.isPresent()) {
            Hall _hall = hallData.get();
            _hall.setWorkspaceId(hall.getWorkspaceId());
            _hall.setCapacity(hall.getCapacity());
            _hall.setDescription(hall.getDescription());
            _hall.setPricePerHour(hall.getPricePerHour());
            return new ResponseEntity<>(hallRepo.save(_hall), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/halls/{id}")
    // @PreAuthorize("hasAnyAuthority('provider:delete', 'admin:delete')")
    public ResponseEntity<HttpStatus> deleteHall(@PathVariable("id") long id) {
        try {
            hallRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
