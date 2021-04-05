package com.epam.brest.dao;

import com.epam.brest.SpringJdbcConfig;
import com.epam.brest.dao.RoleJdbcDAO;
import com.epam.brest.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import({RoleJdbcDAO.class})
@PropertySource({"classpath:dao.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleJdbcDAOTest {

    @Autowired
    private RoleJdbcDAO roleJdbcDAO;

    @Test
    public void findAllRolesTest(){
        List<Role> roles = roleJdbcDAO.findAll();
        assertNotNull(roles);
        assertTrue(roles.size() > 0);
    }

    @Test
    public void findExistEmployeeTest(){
        Optional<Role> role = roleJdbcDAO.findById(1);
        assertTrue(role.isPresent());
        assertEquals("Project Manager", role.get().getRoleName());
    }

    @Test
    public void findNotExistEmployeeTest(){
        Optional<Role> role = roleJdbcDAO.findById(9999);
        assertTrue(role.isEmpty());
    }

    @Test
    public void createNewRoleTest(){
        Role role = new Role (null, "abc");
        Optional<Role> createdRole = roleJdbcDAO.create(role);
        assertTrue(createdRole.isPresent());
        assertEquals("abc", createdRole.get().getRoleName());
    }

    @Test
    public void updateExistRole(){
        Role role = new Role (1, "abc");
        Optional<Role> createdRole = roleJdbcDAO.update(role);
        assertTrue(createdRole.isPresent());
        assertEquals("abc", roleJdbcDAO.findById(1).get().getRoleName());
    }

    @Test
    public void updateNotExistRole(){
        Role role = new Role (999, "abc");
        assertThrows(IllegalArgumentException.class, () ->{
            roleJdbcDAO.update(role);
        });
    }

    @Test
    public void deleteExistEmployee(){
        roleJdbcDAO.delete(1);
        assertEquals(2,roleJdbcDAO.findAll().size());
    }

    @Test
    public void deleteNotExistEmployee(){
        assertThrows(IllegalArgumentException.class, () -> {
            roleJdbcDAO.delete(999);
        });
        assertEquals(3,roleJdbcDAO.findAll().size());
    }

}