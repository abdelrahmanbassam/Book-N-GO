package com.example.book_n_go.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.book_n_go.model.Aminity;
import com.example.book_n_go.repository.AminityRepo;
import com.example.book_n_go.repository.HallRepo;
import com.example.book_n_go.repository.WorkspaceRepo;

@RestController
@RequestMapping("/aminities")
@CrossOrigin(origins = "http://localhost:3000")
public class AminityController {
    @Autowired
    private AminityRepo aminityRepo;

    @GetMapping
    public ResponseEntity<List<Aminity>> getAminities() {
        try {
            List<Aminity> aminities = new ArrayList<Aminity>();
            aminityRepo.findAll().forEach(aminities::add);
            if (aminities.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(aminities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}