package com.epam.brest.web_app;

import com.epam.brest.model.Employee;
import com.epam.brest.model.Role;
import com.epam.brest.model.dto.EmployeeDTO;
import com.epam.brest.service.EmployeeDTOService;
import com.epam.brest.service.EmployeeService;
import com.epam.brest.service.RoleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class EmployeeController {

    private static final Log LOGGER = LogFactory.getLog(EmployeeController.class);

    private final EmployeeDTOService employeeDTOService;

    private final EmployeeService employeeService;

    private final RoleService roleService;

    public EmployeeController(EmployeeDTOService employeeDTOService, EmployeeService employeeService, RoleService roleService) {
        this.employeeDTOService = employeeDTOService;
        this.employeeService = employeeService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/employees")
    public final String employees(Model model) {
        LOGGER.debug("Controller method called to view all Employee");
        model.addAttribute("employees", employeeDTOService.findAll());
        return "employees";
    }

    @GetMapping(value = "/employee/{id}")
    public final String gotoEditEmployeePage(@PathVariable Integer id, Model model) {
        LOGGER.debug("Controller method called to view EditPage Employee with Id: " + id);
        Optional<EmployeeDTO> optionalEmployee = employeeDTOService.findById(id);
        if (optionalEmployee.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("employee", new Employee(
                    optionalEmployee.get().getEmployeeId(),
                    optionalEmployee.get().getFirstName(),
                    optionalEmployee.get().getLastName(),
                    optionalEmployee.get().getMiddleName(),
                    optionalEmployee.get().getEmail(),
                    optionalEmployee.get().getRoles() == null
                            ? null
                            : optionalEmployee.get().getRoles()
                                .stream()
                                .map(Role::getRoleId)
                                .collect(Collectors.toList())));
            model.addAttribute("roleList", roleService.findAll());
            return "employee";
        } else {
            model.addAttribute("statusCodeName", "Bad Request");
            model.addAttribute("statusCode", "400".toCharArray());
            model.addAttribute("errorMessage", "Employee with id: " + id + ", not found");
            return "error";
        }
    }

    @GetMapping(value = "/employee")
    public final String gotoAddEmployeePage(Model model) {
        LOGGER.debug("Controller method called to view AddPage Employee");
        model.addAttribute("isNew", true);
        model.addAttribute("employee", new Employee());
        model.addAttribute("roleList", roleService.findAll());
        return "employee";
    }

    @PostMapping(value = "/employee")
    public String addEmployee(Employee employee,Model model) {
        LOGGER.debug("Controller method called to add new Employee: " + employee.toString());
        employeeService.create(employee);
        return "redirect:/employees";
    }

    @PostMapping(value = "/employee/{id}")
    public String updateEmployee(Employee employee, Model model) {
        LOGGER.debug("Controller method called to add new Employee: " + employee.toString());
        employeeService.update(employee);
        return "redirect:/employees";
    }

    @GetMapping(value = "/employee/{id}/delete")
    public final String deleteEmployeeById(@PathVariable Integer id, Model model) {
        LOGGER.debug("Controller method called to delete Employee: " + id);
        employeeService.delete(id);
        return "redirect:/employees";
    }

}
