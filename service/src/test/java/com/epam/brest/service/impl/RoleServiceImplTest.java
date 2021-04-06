package com.epam.brest.service.impl;

import com.epam.brest.model.Project;
import com.epam.brest.model.Role;
import com.epam.brest.service.ProjectService;
import com.epam.brest.service.RoleService;
import com.epam.brest.test_db.SpringJdbcConfig;
import com.epam.brest.dao.RoleJdbcDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import({RoleServiceImpl.class, RoleJdbcDAO.class})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@ComponentScan(basePackages = {"com.epam.brest.dao", "com.epam.brest.testdb"})
@PropertySource({"classpath:dao.properties"})
@Transactional
class RoleServiceImplTest {

    @Autowired
    private RoleService roleService;

    @Test
    public void shouldFindAllRoles(){
        List<Role> roleList = roleService.findAll();
        assertTrue(roleList.size() > 0);
    }

    @Test
    public void shouldFindRole(){
        Optional<Role> role = roleService.findById(1);
        assertTrue(role.isPresent());
        assertEquals(1, role.get().getRoleId());
    }

    @Test
    public void shouldNotFindRole(){
        Optional<Role> role = roleService.findById(10);
        assertTrue(role.isEmpty());
    }

    @Test
    public void shouldCreateNewRole(){
        Role newRole = new Role(
                null,
                "abc");
        Optional<Role> role = roleService.create(newRole);
        assertTrue(role.isPresent());
        assertNotNull(role.get().getRoleId());
    }

    @Test
    public void shouldUpdateExistRole(){
        Role newRole = new Role(
                1,
                "abc");
        Optional<Role> role = roleService.update(newRole);
        assertTrue(role.isPresent());
        assertEquals("abc", role.get().getRoleName());
    }

    @Test
    public void shouldNotUpdateNotExistProject(){
        Role newRole = new Role(
                99999,
                "abc");
        assertThrows(IllegalArgumentException.class, () -> roleService.update(newRole));
    }

    @Test
    public void shouldNotDeleteNotExistProject(){
        assertThrows(IllegalArgumentException.class, () -> roleService.delete(999));
    }

    @Test
    public void shouldDeleteExistProject(){
        assertDoesNotThrow(() -> roleService.delete(1));
    }

}