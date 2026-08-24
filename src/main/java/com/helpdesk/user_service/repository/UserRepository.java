package main.java.com.helpdesk.user_service.repository;

import main.java.com.helpdesk.user_service.model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
}