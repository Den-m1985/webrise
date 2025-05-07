package com.example.webrise.repository;

import com.example.webrise.model.Subscription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    List<Subscription> findByUserId(Integer userId);

    @Query("SELECT s.serviceName, COUNT(s) as cnt FROM Subscription s GROUP BY s.serviceName ORDER BY cnt DESC")
    List<Object[]> findTopSubscriptions(Pageable pageable);
}
