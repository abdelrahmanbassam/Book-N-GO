package com.example.book_n_go.repository;

import com.example.book_n_go.model.Hall;
import com.example.book_n_go.model.Workspace;

import java.util.List;

import com.example.book_n_go.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface HallRepo extends JpaRepository<Hall, Long>,JpaSpecificationExecutor<Hall> {
    List<Hall> findByWorkspace(Workspace workspace);
    List<Hall> findByWorkspaceId(Long workspaceId);


}
