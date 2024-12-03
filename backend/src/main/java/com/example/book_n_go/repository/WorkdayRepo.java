package com.example.book_n_go.repository;

import com.example.book_n_go.model.Workday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkdayRepo extends JpaRepository<Workday, Long> {

}
