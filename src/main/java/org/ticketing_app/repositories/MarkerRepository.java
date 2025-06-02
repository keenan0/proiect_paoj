package org.ticketing_app.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.ticketing_app.model.Marker;

import java.sql.PreparedStatement;
import java.sql.Statement;

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
        String checkSql = "SELECT COUNT(*) FROM marker WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, marker.getId());

        if (count != null && count == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String insertSql = "INSERT INTO marker (id, latitude, longitude, popup) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(insertSql);
                ps.setLong(1, marker.getId());
                ps.setDouble(2, marker.getLatitude());
                ps.setDouble(3, marker.getLongitude());
                ps.setString(4, marker.getPopUp());
                return ps;
            });
        } else {
            String updateSql = "UPDATE marker SET latitude = ?, longitude = ?, popup = ? WHERE id = ?";
            jdbcTemplate.update(updateSql, marker.getLatitude(), marker.getLongitude(), marker.getPopUp(), marker.getId());
        }

        return marker;
    }
}