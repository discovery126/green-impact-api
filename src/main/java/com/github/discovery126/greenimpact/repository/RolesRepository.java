package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RolesRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByNameRole(String nameRole);
}
