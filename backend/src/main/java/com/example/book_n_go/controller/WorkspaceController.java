package com.example.book_n_go.controller;

import com.example.book_n_go.repository.WorkspaceRepo;
import com.example.book_n_go.model.Workspace;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.*;

import org.springframework.stereotype.Controller;

@RestController
public class WorkspaceController {
    @Autowired
    private WorkspaceRepo workspaceRepo;

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
            Workspace _workspace = workspaceRepo.save(workspace);
            return new ResponseEntity<>(_workspace, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/workspaces/{id}")
    public ResponseEntity<Workspace> updateWorkspace(@PathVariable("id") long id, @RequestBody Workspace workspace) {
        Optional<Workspace> workspaceData = workspaceRepo.findById(id);
        if (workspaceData.isPresent()) {
            Workspace _workspace = workspaceData.get();
            _workspace.setProviderId(workspace.getProviderId());
            _workspace.setLocationId(workspace.getLocationId());
            return new ResponseEntity<>(workspaceRepo.save(_workspace), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/workspaces/{id}")
    public ResponseEntity<HttpStatus> deleteWorkspace(@PathVariable("id") long id) {
        try {
            workspaceRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
