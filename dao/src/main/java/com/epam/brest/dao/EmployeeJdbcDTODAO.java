package com.epam.brest.dao;

import com.epam.brest.EmployeeDTODAO;
import com.epam.brest.model.Role;
import com.epam.brest.model.dto.EmployeeDTO;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeJdbcDTODAO implements EmployeeDTODAO {

    private static final Log LOGGER = LogFactory.getLog(EmployeeJdbcDTODAO.class);

    @Value("${employeeDto.findAllEmployees}")
    private String selectSql;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ResultSetExtractor<List<EmployeeDTO>> setExtractor;

    public EmployeeJdbcDTODAO(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.setExtractor =  new ResultSetExtractor<List<EmployeeDTO>>() {
            @Override
            public List<EmployeeDTO> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Map<Integer, EmployeeDTO> data = new LinkedHashMap<>();
                while (resultSet.next()){
                    Integer employeeId = resultSet.getInt("employeeId");
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String middleName = resultSet.getString("middleName");
                    String email = resultSet.getString("email");
                    Integer roleId = resultSet.getInt("roleId");
                    String roleName = resultSet.getString("roleName");

                    data.putIfAbsent(
                            employeeId,
                            new EmployeeDTO(
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
                    data.get(employeeId).getRoles().add(new Role(roleId,roleName));
                }
                return  List.copyOf(data.values());
            }
        };
    }

    @Override
    public List<EmployeeDTO> findAllEmployee() {
        LOGGER.debug("DAO method called to find all EmployeesDTO");
        return jdbcTemplate.query(selectSql, setExtractor);
    }
}
