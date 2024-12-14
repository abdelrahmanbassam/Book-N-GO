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

import com.example.book_n_go.model.Workspace;
import com.example.book_n_go.repository.WorkspaceRepo;

@RestController
// @PreAuthorize("hasAnyRole('Role.ADMIN.name()', 'Role.CLIENT.name()', 'Role.PROVIDER.name()')")
public class WorkspaceController {
    @Autowired
    private WorkspaceRepo workspaceRepo;

    @GetMapping("/workspaces")
    // @PreAuthorize("hasAnyAuthority('client:read', 'provider:read', 'admin:read')")
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
    // @PreAuthorize("hasAnyAuthority('client:read', 'provider:read', 'admin:read')")
    public ResponseEntity<Workspace> getWorkspaceById(@PathVariable("id") long id) {
        Optional<Workspace> workspaceData = workspaceRepo.findById(id);
        if (workspaceData.isPresent()) {
            return new ResponseEntity<>(workspaceData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/workspaces")
    // @PreAuthorize("hasAnyAuthority('provider:write', 'admin:write')")
    public ResponseEntity<Workspace> createWorkspace(@RequestBody Workspace workspace) {
        try {
            Workspace _workspace = workspaceRepo.save(workspace);
            return new ResponseEntity<>(_workspace, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/workspaces/{id}")
    // @PreAuthorize("hasAnyAuthority('provider:update', 'admin:update')")
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
    // @PreAuthorize("hasAnyAuthority('provider:delete', 'admin:delete')")
    public ResponseEntity<HttpStatus> deleteWorkspace(@PathVariable("id") long id) {
        try {
            workspaceRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
