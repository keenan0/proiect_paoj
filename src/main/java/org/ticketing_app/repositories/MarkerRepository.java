package org.ticketing_app.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.ticketing_app.model.Marker;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class MarkerRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MarkerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Marker findById(long id) {
        String sql = "SELECT * FROM marker WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Marker(
                rs.getLong("id"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getString("popup")
        ), id);
    }

    public Marker save(Marker marker) {
        if (marker.getId() == 0) {
            // insert and get generated id
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String sql = "INSERT INTO marker (latitude, longitude, popup) VALUES (?, ?, ?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setDouble(1, marker.getLatitude());
                ps.setDouble(2, marker.getLongitude());
                ps.setString(3, marker.getPopUp());
                return ps;
            }, keyHolder);
            marker.setId(keyHolder.getKey().longValue());
            return marker;
        } else {
            // update existing marker
            String sql = "UPDATE marker SET latitude = ?, longitude = ?, popup = ? WHERE id = ?";
            jdbcTemplate.update(sql, marker.getLatitude(), marker.getLongitude(), marker.getPopUp(), marker.getId());
            return marker;
        }
    }

    public Optional<Marker> findByLatitudeAndLongitude(double latitude, double longitude) {
        String sql = "SELECT * FROM marker WHERE latitude = ? AND longitude = ?";

        try {
            Marker marker = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Marker(
                    rs.getLong("id"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getString("popup")
            ), latitude, longitude);
            return Optional.of(marker);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}