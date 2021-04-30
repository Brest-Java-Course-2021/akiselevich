package com.epam.brest.dao;

import com.epam.brest.ProjectDAO;
import com.epam.brest.model.Project;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
public class ProjectJdbcDAO implements ProjectDAO {

    private static final Logger LOGGER = LogManager.getLogger(ProjectJdbcDAO.class);

    @Value("${project.select}")
    private String selectSql;

    @Value("${project.create}")
    private String createSql;

    @Value("${project.update}")
    private String updateSql;

    @Value("${project.findById}")
    private String findByIdSql;

    @Value("${project.delete}")
    private String deleteSql;

    @Value("${projectEmployee.create}")
    private String createPESql;

    @Value("${projectEmployee.delete}")
    private String deletePESql;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ResultSetExtractor<List<Project>> setExtractor;

    public ProjectJdbcDAO(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.setExtractor = new ResultSetExtractor<List<Project>>() {
            @Override
            public List<Project> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Map<Integer, Project> data = new LinkedHashMap<Integer, Project>();
                while (resultSet.next()){
                    Integer projectId = resultSet.getInt("projectId");
                    String projectName = resultSet.getString("projectName");
                    LocalDate startDate = resultSet.getObject("startDate", LocalDate.class);
                    LocalDate finishDate = resultSet.getObject("finishDate", LocalDate.class);
                    Integer employeeId = resultSet.getInt("employeeId");

                    data.putIfAbsent(
                            projectId,
                            new Project(
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
                    data.get(projectId).getEmployeeId().add(employeeId);
                }
                return  List.copyOf(data.values());
            }
        };
    }

    @Override
    public List<Project> findAll() {
        LOGGER.debug("DAO method called to find all Projects");
        return jdbcTemplate.query(selectSql, setExtractor);
    }

    @Override
    public Optional<Project> findById(Integer projectId) {
        LOGGER.debug("DAO method called to find Project with Id: {}", projectId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("projectId", projectId);
        List<Project> projects = jdbcTemplate.query(findByIdSql, sqlParameterSource, setExtractor);
        return projects.isEmpty()
                ? Optional.empty()
                : Optional.of(projects.get(0));
    }

    @Override
    public Optional<Project> create(Project project) {
        LOGGER.debug("DAO method called to create Project: {}", project);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(
                Map.of("projectName", project.getProjectName(),
                        "startDate", project.getStartDate(),
                        "finishDate", project.getFinishDate())
        );
        jdbcTemplate.update(createSql, sqlParameterSource, keyHolder);
        Integer projectId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        project.setProjectId(projectId);

        if(project.getEmployeeId() != null) {
            try{
                project.getEmployeeId()
                        .stream()
                        .filter(Objects::nonNull)
                        .forEach(employeeId -> {
                            SqlParameterSource employeeSqlParameterSource = new MapSqlParameterSource(
                                    Map.of("projectId", projectId,
                                            "employeeId", employeeId)
                            );
                            jdbcTemplate.update(createPESql, employeeSqlParameterSource, keyHolder);
                        });
            }catch (DataIntegrityViolationException ex){
                throw new IllegalArgumentException("Employees with Id:" + project.getEmployeeId().toString() + " not exist");
            }
        }
        return Optional.of(project);
    }

    @Override
    public Optional<Project> update(Project project) {
        LOGGER.debug("DAO method called to update Project with Id: {}, new Project: {}", project.getProjectId(), project);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(
                Map.of("projectName", project.getProjectName(),
                        "startDate", project.getStartDate(),
                        "finishDate", project.getFinishDate(),
                        "projectId", project.getProjectId())
        );
        try{
            jdbcTemplate.update(updateSql, sqlParameterSource, keyHolder);

            Integer projectId = Objects.requireNonNull(keyHolder.getKey()).intValue();
            project.setProjectId(projectId);

            sqlParameterSource = new MapSqlParameterSource(
                    Map.of("projectId", projectId)
            );
            jdbcTemplate.update(deletePESql, sqlParameterSource);

            if(project.getEmployeeId() != null) {
                project.getEmployeeId()
                        .stream()
                        .filter(Objects::nonNull)
                        .forEach(employeeId -> {
                            SqlParameterSource employeeSqlParameterSource = new MapSqlParameterSource(
                                    Map.of("projectId", projectId,
                                            "employeeId", employeeId)
                            );
                            jdbcTemplate.update(createPESql, employeeSqlParameterSource, keyHolder);
                        });
            }
        }catch (NullPointerException ex){
            throw new IllegalArgumentException("Project with Id:" + project.getProjectId() + " not exist");
        }catch (DataIntegrityViolationException ex){
            throw new IllegalArgumentException("Employees with Id:" + project.getEmployeeId().toString() + " not exist");
        }

        return Optional.of(project);
    }

    @Override
    public void delete(Integer projectId) {
        LOGGER.debug("DAO method called to delete Project with Id: {}", projectId);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("projectId", projectId);
        List<Project> projects = jdbcTemplate.query(findByIdSql, sqlParameterSource, setExtractor);

        if (projects.isEmpty()){
            throw new IllegalArgumentException("Project with Id:" + projectId + " not exist");
        }

        jdbcTemplate.update(deletePESql,sqlParameterSource);
        jdbcTemplate.update(deleteSql, sqlParameterSource);
    }

}
