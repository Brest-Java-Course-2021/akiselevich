package com.epam.brest.dao;

import com.epam.brest.EmployeeDAO;
import com.epam.brest.model.Employee;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
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
import java.util.*;

@Repository
public class EmployeeJdbcDAO implements EmployeeDAO {

    private static final Log LOGGER = LogFactory.getLog(EmployeeJdbcDAO.class);

    @Value("${employee.select}")
    private String selectSql;

    @Value("${employee.create}")
    private String createSql;

    @Value("${employee.update}")
    private String updateSql;

    @Value("${employee.findById}")
    private String findByIdSql;

    @Value("${employee.delete}")
    private String deleteSql;

    @Value("${employeeRole.create}")
    private String createERSql;

    @Value("${employeeRole.delete}")
    private String deleteERSql;

    @Value("${employeeProject.delete}")
    private String deleteEPSql;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ResultSetExtractor<List<Employee>> setExtractor;

    public EmployeeJdbcDAO(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.setExtractor = new ResultSetExtractor<List<Employee>>() {
            @Override
            public List<Employee> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Map<Integer, Employee> data = new LinkedHashMap<>();
                while (resultSet.next()){
                    Integer employeeId = resultSet.getInt("employeeId");
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String middleName = resultSet.getString("middleName");
                    String email = resultSet.getString("email");
                    Integer roleId = resultSet.getInt("roleId");

                    data.putIfAbsent(
                            employeeId,
                            new Employee(
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    new ArrayList<>()
                            )
                    );

                    data.get(employeeId).setEmployeeId(employeeId);
                    data.get(employeeId).setFirstName(firstName);
                    data.get(employeeId).setLastName(lastName);
                    data.get(employeeId).setMiddleName(middleName);
                    data.get(employeeId).setEmail(email);
                    data.get(employeeId).getRoleId().add(roleId);
                }
                return  List.copyOf(data.values());
            }
        };
    }

    @Override
    public List<Employee> findAll() {
        LOGGER.debug("DAO method called to find all Employees");
        return jdbcTemplate.query(selectSql, setExtractor);
    }

    @Override
    public Optional<Employee> findById(Integer employeeId) {
        LOGGER.debug("DAO method called to find Employee with Id: " + employeeId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("employeeId", employeeId);
        List<Employee> projects = jdbcTemplate.query(findByIdSql, sqlParameterSource, setExtractor);
        return projects.isEmpty() ? Optional.empty() : Optional.of(projects.get(0));
    }

    @Override
    public Optional<Employee> create(Employee employee) {
        LOGGER.debug("DAO method called to create Employee: " + employee.toString());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(
                Map.of("firstName", employee.getFirstName(),
                        "lastName", employee.getLastName(),
                        "middleName", employee.getMiddleName(),
                        "email", employee.getEmail())
        );
        try{
            jdbcTemplate.update(createSql, sqlParameterSource, keyHolder);
            Integer employeeId = Objects.requireNonNull(keyHolder.getKey()).intValue();
            employee.setEmployeeId(employeeId);

            if(employee.getRoleId() != null) {
                employee.getRoleId()
                        .stream()
                        .filter(Objects::nonNull)
                        .forEach(roleId -> {
                            SqlParameterSource roleSqlParameterSource = new MapSqlParameterSource(
                                    Map.of("employeeId", employeeId,
                                            "roleId", roleId)
                            );
                            jdbcTemplate.update(createERSql, roleSqlParameterSource, keyHolder);
                        });
            }
        }catch (DuplicateKeyException ex){
            throw new IllegalArgumentException("Employee with email:" + employee.getEmail() + " already exist");
        }catch (DataIntegrityViolationException ex){
            throw new IllegalArgumentException("Role with Id:" + employee.getRoleId().toString() + " not exist");
        }
        return Optional.of(employee);
    }

    @Override
    public Optional<Employee> update(Employee employee) {
        LOGGER.debug("DAO method called to update Employee with Id: " + employee.getEmployeeId() +
                ", new Project:" + employee.toString());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(
                Map.of("firstName", employee.getFirstName(),
                        "lastName", employee.getLastName(),
                        "middleName", employee.getMiddleName(),
                        "email", employee.getEmail(),
                        "employeeId", employee.getEmployeeId())
        );

        try{
            jdbcTemplate.update(updateSql, sqlParameterSource, keyHolder);
            Integer employeeId = Objects.requireNonNull(keyHolder.getKey()).intValue();
            employee.setEmployeeId(employeeId);

            sqlParameterSource = new MapSqlParameterSource(
                    Map.of("employeeId", employeeId)
            );
            jdbcTemplate.update(deleteERSql, sqlParameterSource);

            if(employee.getRoleId() != null) {
                employee.getRoleId()
                        .stream()
                        .filter(Objects::nonNull)
                        .forEach(roleId -> {
                            SqlParameterSource employeeSqlParameterSource = new MapSqlParameterSource(
                                    Map.of("employeeId", employeeId,
                                            "roleId", roleId)
                            );
                            jdbcTemplate.update(createERSql, employeeSqlParameterSource, keyHolder);
                        });
            }
        }catch (NullPointerException ex){
            throw new IllegalArgumentException("Employee with id:" + employee.getEmployeeId() + " not exist");
        }catch (DuplicateKeyException ex){
            throw new IllegalArgumentException("Employee with email:" + employee.getEmail() + " already exist");
        }catch (DataIntegrityViolationException ex){
            throw new IllegalArgumentException("Role with Id:" + employee.getRoleId().toString() + " not exist");
        }
        return Optional.of(employee);
    }

    @Override
    public void delete(Integer employeeId) {
        LOGGER.debug("DAO method called to delete Employee with Id: " + employeeId);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("employeeId", employeeId);
        List<Employee> employees = jdbcTemplate.query(findByIdSql, sqlParameterSource, setExtractor);

        if (employees.isEmpty()){
            throw new IllegalArgumentException("Employee with Id:" + employeeId + " not exist");
        }

        jdbcTemplate.update(deleteEPSql,sqlParameterSource);
        jdbcTemplate.update(deleteERSql,sqlParameterSource);
        jdbcTemplate.update(deleteSql, sqlParameterSource);
    }
}
