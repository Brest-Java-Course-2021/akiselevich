package com.epam.brest.web_app;

import com.epam.brest.model.Role;
import com.epam.brest.service.RoleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class RoleController {

    private static final Log LOGGER = LogFactory.getLog(RoleController.class);

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value = "/roles")
    public final String employees(Model model) {
        LOGGER.debug("Controller method called to view all Roles");
        model.addAttribute("roles", roleService.findAll());
        return "roles";
    }

    @GetMapping(value = "/role/{id}")
    public final String gotoEditRolePage(@PathVariable Integer id, Model model) {
        LOGGER.debug("Controller method called to view EditPage Role with Id: " + id);
        Optional<Role> optionalRole = roleService.findById(id);
        if (optionalRole.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("role", optionalRole.get());
            return "role";
        } else {
            model.addAttribute("statusCodeName", "Bad Request");
            model.addAttribute("statusCode", "400".toCharArray());
            model.addAttribute("errorMessage", "Role with id: " + id + ", not found");
            return "error";
        }
    }

    @GetMapping(value = "/role")
    public final String gotoAddRolePage(Model model) {
        LOGGER.debug("Controller method called to view AddPage Role");
        model.addAttribute("isNew", true);
        model.addAttribute("role", new Role());
        return "role";
    }

    @PostMapping(value = "/role")
    public String addRole(Role role) {
        LOGGER.debug("Controller method called to add new Role: " + role.toString());
        roleService.create(role);
        return "redirect:/roles";
    }

    @PostMapping(value = "/role/{id}")
    public String updateRole(Role role) {
        LOGGER.debug("Controller method called to update Role: " + role.toString());
        roleService.update(role);
        return "redirect:/roles";
    }

    @GetMapping(value = "/role/{id}/delete")
    public final String deleteRoleById(@PathVariable Integer id, Model model) {
        LOGGER.debug("Controller method called to delete Role: " + id);
        roleService.delete(id);
        return "redirect:/roles";
    }

}
