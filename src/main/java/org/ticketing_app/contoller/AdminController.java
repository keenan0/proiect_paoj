package org.ticketing_app.contoller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ticketing_app.model.User;
import org.ticketing_app.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/users")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();

        // Don't return passwords
        users.forEach(user -> user.setPassword(null));

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.findById(id);

        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        // Don't return the password
        user.setPassword(null);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        if (!id.equals(user.getId())) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "ID in URL does not match ID in request body");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            User updatedUser = userService.update(user);
            // Don't return the password
            updatedUser.setPassword(null);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}