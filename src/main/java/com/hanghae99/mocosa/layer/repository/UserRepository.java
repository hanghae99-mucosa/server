package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);
}
