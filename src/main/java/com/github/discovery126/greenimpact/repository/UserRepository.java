package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByDisplayName(String displayName);

    @Query("SELECT u FROM User u")
    Stream<User> streamAll();
}