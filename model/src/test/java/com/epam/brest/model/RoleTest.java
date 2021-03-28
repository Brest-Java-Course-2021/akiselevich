package com.epam.brest.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleTest {

    @Test
    public void EmptyRoleToStringTest(){
        Role role = new Role();
        assertEquals("Role{roleId=null, roleName='null'}", role.toString());
    }

    @Test
    public void RoleToStringTest(){
        Role role = new Role(1,"abc");
        assertEquals("Role{roleId=1, roleName='abc'}", role.toString());
    }

}