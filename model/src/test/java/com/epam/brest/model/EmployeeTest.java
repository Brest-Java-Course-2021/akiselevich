package com.epam.brest.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeTest {

    @Test
    public void EmptyEmployeeToStringTest(){
        Employee employee = new Employee();
        assertEquals("Employee{employeeId=null, firstName='null', lastName='null', middleName='null', email='null', roleId=null}",employee.toString());
    }

    @Test
    public void EmployeeToStringTest(){
        Employee employee = new Employee(1,"abc","abc","abc","abc@abc.com",null);
        assertEquals("Employee{employeeId=1, firstName='abc', lastName='abc', middleName='abc', email='abc@abc.com', roleId=null}",employee.toString());
    }

}