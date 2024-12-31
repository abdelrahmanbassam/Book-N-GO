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

import com.example.book_n_go.enums.Permission;
import com.example.book_n_go.model.Location;
import com.example.book_n_go.model.User;
import com.example.book_n_go.repository.LocationRepo;
import com.example.book_n_go.service.AuthService;

@RestController
public class LocationController {
    @Autowired
    private LocationRepo locationRepo;

    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getAllLocations() {
        try {
            List<Location> locations = new ArrayList<Location>();
            locationRepo.findAll().forEach(locations::add);
            if (locations.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(locations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/locations/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable("id") long id) {

        Optional<Location> locationData = locationRepo.findById(id);
        if (locationData.isPresent()) {
            return new ResponseEntity<>(locationData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/locations")
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        try {
            if (!AuthService.userHasPermission(Permission.PROVIDER_WRITE)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            Location _location = locationRepo.save(location);
            return new ResponseEntity<>(_location, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/locations/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable("id") long id, @RequestBody Location location) {
        Optional<Location> locationData = locationRepo.findById(id);
        if (locationData.isPresent()) {
            if (!AuthService.userHasPermission(Permission.ADMIN_UPDATE)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            Location _location = locationData.get();
            _location.setDepartmentNumber(location.getDepartmentNumber());
            _location.setStreet(location.getStreet());
            _location.setCity(location.getCity());
            return new ResponseEntity<>(locationRepo.save(_location), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/locations/{id}")
    public ResponseEntity<HttpStatus> deleteLocation(@PathVariable("id") long id) {
        try {
            if (!AuthService.userHasPermission(Permission.ADMIN_DELETE)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            locationRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}