package com.example.Task_III.repository;

import com.example.Task_III.entity.Restock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestockRepository extends JpaRepository<Restock, Long> {
}
