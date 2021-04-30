package com.epam.brest.dao;

import com.epam.brest.RoleDAO;
import com.epam.brest.model.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RoleJdbcDAO implements RoleDAO {

    private static final Logger LOGGER = LogManager.getLogger(RoleJdbcDAO.class);

    @Value("${role.select}")
    private String selectSql;

    @Value("${role.create}")
    private String createSql;

    @Value("${role.update}")
    private String updateSql;

    @Value("${role.findById}")
    private String findByIdSql;

    @Value("${role.delete}")
    private String deleteSql;

    @Value("${roleEmployee.delete}")
    private String deleteERSql;


    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Role> rowMapper;

    public RoleJdbcDAO(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.rowMapper = new RowMapper<Role>() {
            @Override
            public Role mapRow(ResultSet resultSet, int i) throws SQLException {
                Integer roleId = resultSet.getInt("roleId");
                String roleName = resultSet.getString("roleName");
                return new Role(roleId,roleName);
            }
        };

    }

    @Override
    public List<Role> findAll() {
        LOGGER.debug("DAO method called to find all Roles");
        return jdbcTemplate.query(selectSql, rowMapper);
    }

    @Override
    public Optional<Role> findById(Integer roleId) {
        LOGGER.debug("DAO method called to find Role with Id: {}", roleId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("roleId", roleId);
        List<Role> roles = jdbcTemplate.query(findByIdSql, sqlParameterSource, rowMapper);
        return roles.isEmpty() ? Optional.empty() : Optional.of(roles.get(0));
    }

    @Override
    public Optional<Role> create(Role role) {
        LOGGER.debug("DAO method called to create Role: {}", role);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("roleName", role.getRoleName());
        jdbcTemplate.update(createSql, sqlParameterSource, keyHolder);
        Integer roleId = Objects.requireNonNull(keyHolder.getKey()).intValue();

        role.setRoleId(roleId);

        return Optional.of(role);
    }

    @Override
    public Optional<Role> update(Role role) {
        LOGGER.debug("DAO method called to update Role with Id: {}, new Role: {}", role.getRoleId(), role);
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource(
                    Map.of("roleName", role.getRoleName(),
                            "roleId", role.getRoleId())
            );
            jdbcTemplate.update(updateSql, sqlParameterSource, keyHolder);
            Integer roleId = Objects.requireNonNull(keyHolder.getKey()).intValue();

            role.setRoleId(roleId);

        }catch (NullPointerException ex){
            throw new IllegalArgumentException("Role with email:" + role.getRoleId() + " not found");
        }
        return Optional.of(role);
    }

    @Override
    public void delete(Integer roleId) {
        LOGGER.debug("DAO method called to delete Role with Id: {}", roleId);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("roleId", roleId);
        List<Role> roles = jdbcTemplate.query(findByIdSql, sqlParameterSource, rowMapper);

        if (roles.isEmpty()){
            throw new IllegalArgumentException("Role with Id:" + roleId + " not exist");
        }

        jdbcTemplate.update(deleteERSql,sqlParameterSource);
        jdbcTemplate.update(deleteSql, sqlParameterSource);
    }

}
