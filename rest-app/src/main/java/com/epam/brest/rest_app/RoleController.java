package com.epam.brest.rest_app;

import com.epam.brest.model.Role;
import com.epam.brest.service.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@CrossOrigin
@RestController
public class RoleController {

    private static final Logger LOGGER = LogManager.getLogger(RoleController.class);

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value = "/roles")
    public Collection<Role> roles() {
        LOGGER.debug("Controller method called to view all Roles");
        return roleService.findAll();
    }

    @GetMapping(value = "/roles/{id}")
    public ResponseEntity<Role> findById(@PathVariable Integer id) {
        LOGGER.debug("Controller method called to view Role by Id: {}", id);
        Optional<Role> optional = roleService.findById(id);
        return optional.isPresent()
                ? new ResponseEntity<>(optional.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/roles", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        LOGGER.debug("Controller method called to create new Role: {}", role);
        return new ResponseEntity<>(roleService.create(role).get(), HttpStatus.CREATED);
    }

    @PutMapping(value = "/roles", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Role> updateRole(@RequestBody Role role) {
        LOGGER.debug("Controller method called to update Role: {}", role);
        return new ResponseEntity<>(roleService.update(role).get(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/roles/{id}")
    public HttpStatus deleteRole(@PathVariable Integer id) {
        LOGGER.debug("Controller method called to delete Role with Id: {}", id);
        roleService.delete(id);
        return HttpStatus.OK;
    }

}
