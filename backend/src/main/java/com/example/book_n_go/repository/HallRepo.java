package com.example.book_n_go.repository;

import com.example.book_n_go.models.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepo extends JpaRepository<Hall, Long> {

}
