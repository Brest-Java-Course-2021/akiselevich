package com.epam.brest.web_app;

import com.epam.brest.model.Employee;
import com.epam.brest.model.dto.EmployeeDTO;
import com.epam.brest.service.EmployeeDTOService;
import com.epam.brest.service.EmployeeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class EmployeeController {

    private static final Log LOGGER = LogFactory.getLog(EmployeeController.class);

    private final EmployeeDTOService employeeDTOService;

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeDTOService employeeDTOService, EmployeeService employeeService) {
        this.employeeDTOService = employeeDTOService;
        this.employeeService = employeeService;
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
            model.addAttribute("employee", optionalEmployee.get());
            return "employee";
        } else {
            // TODO not found error
            return "redirect:employees";
        }
    }

    @GetMapping(value = "/employee")
    public final String gotoAddEmployeePage(Model model) {
        LOGGER.debug("Controller method called to view AddPage Employee");
        model.addAttribute("isNew", true);
        model.addAttribute("employee", new Employee());
        return "employee";
    }

    @PostMapping(value = "/employee")
    public String addEmployee(Employee employee) {
        LOGGER.debug("Controller method called to add new Employee: " + employee.toString());
        employeeService.create(employee);
        return "redirect:/employees";
    }

    @PostMapping(value = "/employee/{id}")
    public String updateEmployee(Employee employee) {
        LOGGER.debug("Controller method called to update Employee: " + employee.toString());
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
