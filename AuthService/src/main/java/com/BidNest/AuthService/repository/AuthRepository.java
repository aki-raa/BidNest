package com.BidNest.AuthService.repository;

import com.BidNest.AuthService.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel>findUserByEmail(String email);
}
