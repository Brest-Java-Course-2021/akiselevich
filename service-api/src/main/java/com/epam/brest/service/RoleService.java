package com.epam.brest.service;

import com.epam.brest.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> findAll();

    Optional<Role> findById(Integer roleId);

    Optional<Role> create(Role role);

    Optional<Role> update(Role role);

    void delete(Integer roleId);

}
