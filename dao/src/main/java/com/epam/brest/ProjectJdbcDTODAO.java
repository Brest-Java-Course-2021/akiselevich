package com.epam.brest;

import com.epam.brest.model.Project;
import com.epam.brest.model.Role;
import com.epam.brest.model.dto.EmployeeDTO;
import com.epam.brest.model.dto.ProjectDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProjectJdbcDTODAO  implements  ProjectDTODAO{

    private static final Log LOGGER = LogFactory.getLog(ProjectJdbcDTODAO.class);

    @Value("${projectDto.findAllWithCountOfEmployees}")
    private String selectSql;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ResultSetExtractor<List<ProjectDTO>> setExtractor;

    public ProjectJdbcDTODAO(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.setExtractor = new ResultSetExtractor<List<ProjectDTO>>() {
            @Override
            public List<ProjectDTO> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Map<Integer, ProjectDTO> data = new LinkedHashMap<>();
                Map<Integer, EmployeeDTO> employees = new LinkedHashMap<>();
                while (resultSet.next()){
                    Integer projectId = resultSet.getInt("projectId");
                    String projectName = resultSet.getString("projectName");
                    LocalDateTime startDate = resultSet.getObject("startDate", LocalDateTime.class);
                    LocalDateTime finishDate = resultSet.getObject("finishDate", LocalDateTime.class);
                    Integer employeeId = resultSet.getInt("employeeId");
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String middleName = resultSet.getString("middleName");
                    String email = resultSet.getString("email");
                    Integer roleId = resultSet.getInt("roleId");
                    String roleName = resultSet.getString("roleName");
                    Integer numberOfEmployee = resultSet.getInt("numberOfEmployee");

                    if (!data.containsKey(projectId)){
                        employees.clear();
                    }

                    data.putIfAbsent(
                            projectId,
                            new ProjectDTO(
                                    null,
                                    null,
                                    null,
                                    null,
                                    new ArrayList<>(),
                                    null)
                    );

                    employees.putIfAbsent(employeeId,
                            new EmployeeDTO(
                                    null,
                            null,
                            null,
                            null,
                            null,
                            new ArrayList<>())
                    );

                    data.get(projectId).setProjectId(projectId);
                    data.get(projectId).setProjectName(projectName);
                    data.get(projectId).setStartDate(startDate);
                    data.get(projectId).setFinishDate(finishDate);
                    data.get(projectId).setEmployees(List.copyOf(employees.values()));
                    data.get(projectId).setNumberOfEmployees(numberOfEmployee);

                    employees.get(employeeId).setEmployeeId(employeeId);
                    employees.get(employeeId).setFirstName(firstName);
                    employees.get(employeeId).setLastName(lastName);
                    employees.get(employeeId).setMiddleName(middleName);
                    employees.get(employeeId).setEmail(email);
                    employees.get(employeeId).getRoles().add(new Role(roleId,roleName));
                }
                return  List.copyOf(data.values());
            }
        };
    }

    @Override
    public List<ProjectDTO> findAllProjectWithEmployeeCount() {
        LOGGER.debug("DAO method called to find all ProjectDTO");
        return jdbcTemplate.query(selectSql, setExtractor);
    }
}
