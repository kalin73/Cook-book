package org.example.cookbook.repository;

import org.example.cookbook.model.entity.LoginLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoginLogRepository extends JpaRepository<LoginLogEntity, UUID> {
}
