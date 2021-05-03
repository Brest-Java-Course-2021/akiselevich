package com.epam.brest.service.rest;

import com.epam.brest.model.Role;
import com.epam.brest.service.RoleService;
import com.epam.brest.service.rest.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.service.rest.config.TestConfig.ROLES_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class RoleServiceRestTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RoleService roleService;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldFindAllRoles() throws Exception {
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(ROLES_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createRole(0),
                                createRole(1)))));

        List<Role> roles = roleService.findAll();

        mockServer.verify();
        assertNotNull(roles);
        assertTrue(roles.size() > 0);
    }

    @Test
    public void shouldFindRoleById() throws Exception {
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(ROLES_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(createRole(id))));

        Optional<Role> optionalRole = roleService.findById(id);

        mockServer.verify();
        assertTrue(optionalRole.isPresent());
        assertEquals(optionalRole.get().getRoleId(), id);
    }

    @Test
    public void shouldCreateRole() throws Exception {
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(ROLES_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(createRole(1))));

        Optional<Role> optionalRole = roleService.create(createRole(null));

        mockServer.verify();
        assertTrue(optionalRole.isPresent());
        assertEquals(optionalRole.get().getRoleId(), 1);
    }

    @Test
    public void shouldUpdateRole() throws Exception {

        Integer id = 1;
        Role role = new Role(null, RandomStringUtils.randomAlphabetic(45));
        role.setRoleId(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(ROLES_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(role)));

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(ROLES_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(role)));

        Optional<Role> optionalRole = roleService.update(role);
        Optional<Role> updatedRole = roleService.findById(id);

        mockServer.verify();
        assertTrue(optionalRole.isPresent());
        assertTrue(updatedRole.isPresent());
        assertEquals(updatedRole.get().getRoleId(), id);
        assertEquals(updatedRole.get().getRoleName(), role.getRoleName());
    }

    private Role createRole(Integer index) {
        Role role = new Role();
        role.setRoleId(index);
        role.setRoleName("role" + index);
        return role;
    }

}