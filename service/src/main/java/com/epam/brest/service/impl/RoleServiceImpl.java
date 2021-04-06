package com.epam.brest.service.impl;

import com.epam.brest.RoleDAO;
import com.epam.brest.model.Role;
import com.epam.brest.service.RoleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private static final Log LOGGER = LogFactory.getLog(RoleServiceImpl.class);

    private final RoleDAO roleDAO;

    public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public List<Role> findAll() {
        LOGGER.debug("Service method called to find all Role");
        return roleDAO.findAll();
    }

    @Override
    public Optional<Role> findById(Integer roleId) {
        LOGGER.debug("Service method called to find Role by Id:" + roleId);
        return roleDAO.findById(roleId);
    }

    @Override
    public Optional<Role> create(Role role) {
        LOGGER.debug("Service method called to create Role:" + role.toString());
        return roleDAO.create(role);
    }

    @Override
    public Optional<Role> update(Role role) {
        LOGGER.debug("Service method called to update Role with id:" + role.getRoleId() + ", new Role:" + role.toString());
        return roleDAO.update(role);
    }

    @Override
    public void delete(Integer roleId) {
        LOGGER.debug("Service method called to delete Role by Id:" + roleId);
        roleDAO.delete(roleId);
    }

}
