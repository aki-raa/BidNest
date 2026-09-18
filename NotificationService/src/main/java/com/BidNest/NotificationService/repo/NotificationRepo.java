package com.BidNest.NotificationService.repo;

import com.BidNest.NotificationService.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepo extends JpaRepository<NotificationLog, UUID> {

    List<NotificationLog> findByUserIdOrderByCreatedAtDesc(UUID userId);
}