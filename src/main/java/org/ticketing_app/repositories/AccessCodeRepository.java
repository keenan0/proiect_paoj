package org.ticketing_app.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.ticketing_app.access_code.AccessCode;
import org.ticketing_app.access_code.CustomAccessCode;
import org.ticketing_app.access_code.QrAccessCode;
import org.ticketing_app.model.TicketingEvent;
import org.ticketing_app.model.User;
import org.ticketing_app.user.CustomUserRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class AccessCodeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CustomUserRepository userRepository;
    private final TicketingEventRepository eventRepository;

    @Autowired
    public AccessCodeRepository(JdbcTemplate jdbcTemplate,
                                    CustomUserRepository userRepository,
                                    TicketingEventRepository eventRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public AccessCode save(AccessCode accessCode) {
        if (accessCode.getId() == 0) {
            return insert(accessCode);
        } else {
            return update(accessCode);
        }
    }

    private AccessCode insert(AccessCode accessCode) {
        String sql = "INSERT INTO access_code (code, generated_at, user_id, event_id, type) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, accessCode.getCode());
            ps.setTimestamp(2, Timestamp.valueOf(accessCode.getGeneratedAt()));
            ps.setLong(3, accessCode.getOwner().getId());
            ps.setObject(4, accessCode.getEvent() != null ? accessCode.getEvent().getId() : null);
            ps.setString(5, accessCode.getClass().getSimpleName());
            return ps;
        }, keyHolder);

        Number id = (Number) keyHolder.getKeys().get("ID");
        accessCode.setId(id.longValue());

        return accessCode;
    }

    private AccessCode update(AccessCode accessCode) {
        String sql = "UPDATE access_code SET code = ?, generated_at = ?, user_id = ?, event_id = ?, type = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                accessCode.getCode(),
                Timestamp.valueOf(accessCode.getGeneratedAt()),
                accessCode.getOwner().getId(),
                accessCode.getEvent() != null ? accessCode.getEvent().getId() : null,
                accessCode.getClass().getSimpleName(),
                accessCode.getId());
        return accessCode;
    }

    public Optional<AccessCode> findById(Long id) {
        String sql = "SELECT * FROM access_code WHERE id = ?";
        try {
            AccessCode accessCode = jdbcTemplate.queryForObject(sql, this::mapRowToAccessCode, id);
            return Optional.ofNullable(accessCode);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<AccessCode> findAll() {
        return jdbcTemplate.query("SELECT * FROM access_code", this::mapRowToAccessCode);
    }

    public List<AccessCode> findByUserId(Long userId) {
        String sql = "SELECT * FROM access_code WHERE user_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToAccessCode, userId);
    }

    public List<AccessCode> findByEventId(Long eventId) {
        String sql = "SELECT * FROM access_code WHERE event_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToAccessCode, eventId);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM access_code WHERE id = ?", id);
    }

    public void deleteByUserId(Long userId) {
        jdbcTemplate.update("DELETE FROM access_code WHERE user_id = ?", userId);
    }

    public void deleteByEventId(Long eventId) {
        jdbcTemplate.update("DELETE FROM access_code WHERE event_id = ?", eventId);
    }

    private AccessCode mapRowToAccessCode(ResultSet rs, int rowNum) throws SQLException {
        String type = rs.getString("type");
        AccessCode accessCode;

        accessCode = switch(type.toLowerCase()) {
            case "qr" -> new QrAccessCode();
            case "custom" -> new CustomAccessCode();
            default -> null;
        };

        accessCode.setId(rs.getLong("id"));
        accessCode.setCode(rs.getString("code"));
        accessCode.setGeneratedAt(rs.getTimestamp("generated_at").toLocalDateTime());

        Long userId = rs.getLong("user_id");
        userRepository.findById(userId).ifPresent(accessCode::setOwner);

        Long eventId = rs.getObject("event_id", Long.class);
        if (eventId != null) {
            eventRepository.findById(eventId).ifPresent(accessCode::setEvent);
        }

        return accessCode;
    }
}