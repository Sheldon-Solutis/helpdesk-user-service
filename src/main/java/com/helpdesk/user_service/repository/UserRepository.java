package com.helpdesk.user_service.repository;

import com.helpdesk.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{

    List<User> findAllByActiveTrue();
    User findByEmail(String email);
    Boolean existsByEmailAndIdNot(String Email, Long id);
    Boolean existsByEmail(String Email);
}