package org.ticketing_app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.ticketing_app.model.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomUserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setEnabled(rs.getBoolean("enabled"));
        user.setRole(rs.getString("role"));
        return user;
    };

    @Autowired
    public CustomUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createUserTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "password VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL UNIQUE, " +
                "enabled BOOLEAN DEFAULT TRUE, " +
                "role VARCHAR(20) DEFAULT 'ROLE_USER'" +
                ")");
    }

    public User save(User user) {
        if (user.getId() == null) {
            return insertUser(user);
        } else {
            return updateUser(user);
        }
    }

    private User insertUser(User user) {
        String sql = "INSERT INTO users (username, password, email, enabled, role) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setBoolean(4, user.isEnabled());
            ps.setString(5, user.getRole());
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    private User updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, enabled = ?, role = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.isEnabled(),
                user.getRole(),
                user.getId());
        return user;
    }

    public Optional<User> findById(Long id) {
        try {
            User user = jdbcTemplate.queryForObject(
                    "SELECT * FROM users WHERE id = ?",
                    userRowMapper,
                    id
            );
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByUsername(String username) {
        try {
            User user = jdbcTemplate.queryForObject(
                    "SELECT * FROM users WHERE username = ?",
                    userRowMapper,
                    username
            );
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        try {
            User user = jdbcTemplate.queryForObject(
                    "SELECT * FROM users WHERE email = ?",
                    userRowMapper,
                    email
            );
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }

    public boolean existsByUsername(String username) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username = ?",
                Integer.class,
                username
        );
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE email = ?",
                Integer.class,
                email
        );
        return count != null && count > 0;
    }
}