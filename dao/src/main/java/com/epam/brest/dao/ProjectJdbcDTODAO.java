package com.epam.brest.dao;

import com.epam.brest.ProjectDTODAO;
import com.epam.brest.model.Filter;
import com.epam.brest.model.Role;
import com.epam.brest.model.dto.EmployeeDTO;
import com.epam.brest.model.dto.ProjectDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ProjectJdbcDTODAO  implements ProjectDTODAO {

    private static final Logger LOGGER = LogManager.getLogger(ProjectJdbcDTODAO.class);

    @Value("${projectDto.findAllWithCountOfEmployees}")
    private String selectSql;

    @Value("${projectDto.findProjectWithEmployeeById}")
    private String selectByIdSql;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ResultSetExtractor<List<ProjectDTO>> setExtractor;

    public ProjectJdbcDTODAO(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.setExtractor = new ResultSetExtractor<List<ProjectDTO>>() {
            @Override
            public List<ProjectDTO> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Map<Integer, ProjectDTO> data = new LinkedHashMap<Integer, ProjectDTO>();
                Map<Integer, EmployeeDTO> employees = new LinkedHashMap<>();
                while (resultSet.next()){
                    Integer projectId = resultSet.getInt("projectId");
                    String projectName = resultSet.getString("projectName");
                    LocalDate startDate = resultSet.getObject("startDate", LocalDate.class);
                    LocalDate finishDate = resultSet.getObject("finishDate", LocalDate.class);
                    Integer employeeId = resultSet.getInt("employeeId");
                    employeeId = employeeId == 0 ? null : employeeId;
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String middleName = resultSet.getString("middleName");
                    String email = resultSet.getString("email");
                    Integer roleId = resultSet.getInt("roleId");
                    roleId = roleId == 0 ? null : roleId;
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
                    employees.get(employeeId).getRoles().add(new Role(roleId, roleName));
                }
                return  List.copyOf(data.values())
                        .stream()
                        .peek(projectDTO ->
                                projectDTO.setEmployees(
                                        projectDTO.getEmployees().get(0).getEmployeeId() == null
                                                ? null
                                                : projectDTO.getEmployees()
                                                    .stream()
                                                    .peek(employeeDTO ->
                                                        employeeDTO.setRoles(
                                                                employeeDTO.getRoles().get(0).getRoleId() == null
                                                                        ? null
                                                                        : employeeDTO.getRoles()))
                                                    .collect(Collectors.toList())))
                        .collect(Collectors.toList());
            }
        };
    }

    @Override
    public List<ProjectDTO> findAllProjectWithEmployeeCount(Filter filter) {
        LOGGER.debug("DAO method called to find all ProjectDTO");
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(
                Map.of("startDate", filter == null || filter.getStartDate() == null
                                ? "0000-01-01"
                                : filter.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        "finishDate", filter == null || filter.getFinishDate() == null
                                ? "9999-12-31"
                                : filter.getFinishDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        );
        return jdbcTemplate.query(selectSql, sqlParameterSource, setExtractor);
    }

    @Override
    public Optional<ProjectDTO> findProjectWithEmployeeById(Integer projectId) {
        LOGGER.debug("DAO method called to find ProjectDTO by Id");
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("projectId", projectId);
        List<ProjectDTO> projectDTOList = jdbcTemplate.query(selectByIdSql, sqlParameterSource, setExtractor);
        return projectDTOList.isEmpty()
                ? Optional.empty()
                : Optional.of(projectDTOList.get(0));
    }

}
