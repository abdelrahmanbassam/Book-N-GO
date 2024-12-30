package com.example.book_n_go.controller;

import com.example.book_n_go.repository.LocationRepo;
import com.example.book_n_go.repository.WorkspaceRepo;
import com.example.book_n_go.enums.Permission;
import com.example.book_n_go.model.Location;
import com.example.book_n_go.model.Workspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContext;

import java.security.Security;
import java.util.*;

import com.example.book_n_go.model.Location;
import com.example.book_n_go.model.User;
import com.example.book_n_go.model.Workspace;
import com.example.book_n_go.repository.LocationRepo;
import com.example.book_n_go.repository.WorkspaceRepo;
import com.example.book_n_go.service.AuthService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class WorkspaceController {
    @Autowired
    private WorkspaceRepo workspaceRepo;
    @Autowired
    private LocationRepo locationRepo;

    @GetMapping("/workspaces")
    public ResponseEntity<List<Workspace>> getWorkspaces() {
        try {
            List<Workspace> workspaces = new ArrayList<Workspace>();
            workspaceRepo.findAll().forEach(workspaces::add);
            if (workspaces.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(workspaces, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/workspaces/{id}")
    public ResponseEntity<Workspace> getWorkspaceById(@PathVariable("id") long id) {
        Optional<Workspace> workspaceData = workspaceRepo.findById(id);
        if (workspaceData.isPresent()) {
            return new ResponseEntity<>(workspaceData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/workspaces")
    public ResponseEntity<Workspace> createWorkspace(@RequestBody Workspace workspace) {
        try {
            if(!AuthService.userHasPermission(Permission.PROVIDER_WRITE)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            User provider = AuthService.getRequestUser();
            workspace.setProvider(provider);
            Location location = locationRepo.save(workspace.getLocation());
            workspace.setLocation(location);
            Workspace _workspace = workspaceRepo.save(workspace);
            return new ResponseEntity<>(_workspace, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/workspaces/{id}")
    public ResponseEntity<Workspace> updateWorkspace(@PathVariable("id") long id, @RequestBody Workspace workspace) {

        if(!AuthService.userHasPermission(Permission.PROVIDER_UPDATE) || AuthService.getRequestUser().getId() != workspace.getProvider().getId()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Workspace> workspaceData = workspaceRepo.findById(id);
        if (workspaceData.isPresent()) {
            Workspace _workspace = workspaceData.get();
            _workspace.setName(workspace.getName());
            _workspace.setDescription(workspace.getDescription());
            Optional<Location> locationData = locationRepo.findById(workspace.getLocation().getId());
            if (locationData.isPresent()) {
                Location location = locationData.get();
                if (location.getCity() != workspace.getLocation().getCity()
                        || location.getStreet() != workspace.getLocation().getStreet()
                        || location.getDepartmentNumber() != workspace.getLocation().getDepartmentNumber()) {
                    location.setCity(workspace.getLocation().getCity());
                    location.setStreet(workspace.getLocation().getStreet());
                    location.setDepartmentNumber(workspace.getLocation().getDepartmentNumber());
                    locationRepo.save(location);
                }
            } else {
                locationRepo.save(workspace.getLocation());
            }
            return new ResponseEntity<>(workspaceRepo.save(_workspace), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/workspaces/{id}")
    public ResponseEntity<HttpStatus> deleteWorkspace(@PathVariable("id") long id) {
        if(!AuthService.userHasPermission(Permission.PROVIDER_DELETE) || AuthService.getRequestUser().getId() != workspaceRepo.findById(id).get().getProvider().getId()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            workspaceRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}