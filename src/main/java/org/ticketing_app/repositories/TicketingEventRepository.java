package org.ticketing_app.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.ticketing_app.model.Marker;
import org.ticketing_app.model.TicketingEvent;
import org.ticketing_app.model.User;
import org.ticketing_app.services.MarkerService;

import java.sql.*;
import java.util.*;

@Repository
public class TicketingEventRepository {

    private final JdbcTemplate jdbcTemplate;
    private final MarkerService markerService;

    @Autowired
    public TicketingEventRepository(JdbcTemplate jdbcTemplate, MarkerService markerService) {
        this.jdbcTemplate = jdbcTemplate;
        this.markerService = markerService;
    }

    public TicketingEvent save(TicketingEvent event) {
        // Saves/inserts a new event
        Marker marker = markerService.saveOrUpdate(event.getMarker());

        String sql = "INSERT INTO event (title, description, marker_id, posted_user_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, event.getTitle());
            ps.setString(2, event.getDescription());
            ps.setLong(3, marker.getId());
            ps.setLong(4, event.getUser().getId());
            return ps;
        }, keyHolder);

        event.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return event;
    }

    public Optional<TicketingEvent> findById(Long id) {
        String sql = """
                SELECT e.id AS event_id, e.title, e.description,
                       e.marker_id,
                       u.id AS user_id, u.username, u.password, u.email, u.role, u.enabled
                FROM event e
                JOIN users u ON e.posted_user_id = u.id
                WHERE e.id = ?
                """;

        try {
            TicketingEvent event = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                long markerId = rs.getLong("marker_id");
                Marker marker = markerService.findById(markerId);

                User user = new User(
                        rs.getLong("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getBoolean("enabled"),
                        rs.getString("role")
                );

                TicketingEvent ev = new TicketingEvent();
                ev.setId(rs.getLong("event_id"));
                ev.setTitle(rs.getString("title"));
                ev.setDescription(rs.getString("description"));
                ev.setMarker(marker);
                ev.setUser(user);
                return ev;
            }, id);

            return Optional.ofNullable(event);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<TicketingEvent> findAll() {
        String sql = """
                SELECT e.id AS event_id, e.title, e.description,
                       e.marker_id,
                       u.id AS user_id, u.username, u.password, u.email, u.role, u.enabled
                FROM event e
                JOIN users u ON e.posted_user_id = u.id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            long markerId = rs.getLong("marker_id");
            Marker marker = markerService.findById(markerId);

            User user = new User(
                    rs.getLong("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getBoolean("enabled"),
                    rs.getString("role")
            );

            TicketingEvent ev = new TicketingEvent();
            ev.setId(rs.getLong("event_id"));
            ev.setTitle(rs.getString("title"));
            ev.setDescription(rs.getString("description"));
            ev.setMarker(marker);
            ev.setUser(user);
            return ev;
        });
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM event WHERE id = ?", id);
    }

    public List<TicketingEvent> findByUserId(Long userId) {
        String sql = """
                SELECT e.id AS event_id, e.title, e.description,
                       e.marker_id,
                       u.id AS user_id, u.username, u.password, u.email, u.role, u.enabled
                FROM event e
                JOIN users u ON e.posted_user_id = u.id
                WHERE e.posted_user_id = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            long markerId = rs.getLong("marker_id");
            Marker marker = markerService.findById(markerId);

            User user = new User(
                    rs.getLong("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getBoolean("enabled"),
                    rs.getString("role")
            );

            TicketingEvent ev = new TicketingEvent();
            ev.setId(rs.getLong("event_id"));
            ev.setTitle(rs.getString("title"));
            ev.setDescription(rs.getString("description"));
            ev.setMarker(marker);
            ev.setUser(user);
            return ev;
        }, userId);
    }

    public List<TicketingEvent> findByMarkerId(Long markerId) {
        String sql = """
            SELECT e.id AS event_id, e.title, e.description,
                   e.marker_id,
                   u.id AS user_id, u.username, u.password, u.email, u.role, u.enabled
            FROM event e
            JOIN users u ON e.posted_user_id = u.id
            WHERE e.marker_id = ?
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Marker marker = markerService.findById(markerId); // optional: optimize if reused in loop

            User user = new User(
                    rs.getLong("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getBoolean("enabled"),
                    rs.getString("role")
            );

            TicketingEvent ev = new TicketingEvent();
            ev.setId(rs.getLong("event_id"));
            ev.setTitle(rs.getString("title"));
            ev.setDescription(rs.getString("description"));
            ev.setMarker(marker);
            ev.setUser(user);
            return ev;
        }, markerId);
    }

}
