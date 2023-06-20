package com.example.demo.repository;

import com.example.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCreatedGreaterThan(LocalDate startDate);
    List<Order> findByCreatedLessThan(LocalDate endDate);
    List<Order> findByCreatedBetween(LocalDate startDate, LocalDate endDate);
}
