package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
