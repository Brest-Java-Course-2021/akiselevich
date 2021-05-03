package com.epam.brest.service.rest;

import com.epam.brest.model.Role;
import com.epam.brest.service.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceRest implements RoleService {

    private static final Logger LOGGER = LogManager.getLogger(RoleServiceRest.class);

    private final String url;

    private final RestTemplate restTemplate;

    public RoleServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Role> findAll() {
        LOGGER.debug("Rest service method called to find all Role");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<Role>) responseEntity.getBody();
    }

    @Override
    public Optional<Role> findById(Integer roleId) {
        LOGGER.debug("Rest service method called to find Role by Id: {}", roleId);
        ResponseEntity<Role> responseEntity =
                restTemplate.getForEntity(url + "/" + roleId, Role.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Optional<Role> create(Role role) {
        LOGGER.debug("Rest service method called to create Role: {}", role);
        ResponseEntity<Role> responseEntity = restTemplate.postForEntity(url, role, Role.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Optional<Role> update(Role role) {
        LOGGER.debug("Rest service method called to update Role with id: {}, new Role: {}", role.getRoleId(), role);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Role> entity = new HttpEntity<>(role, headers);
        ResponseEntity<Role> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, entity, Role.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public void delete(Integer roleId) {
        LOGGER.debug("Rest service method called to delete Role by Id: {}", roleId);
        restTemplate.delete(url + "/" + roleId);
    }

}
