package org.ticketing_app.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ticketing_app.model.User;
import org.ticketing_app.user.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            // Don't return the password in the response
            registeredUser.setPassword(null);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> userOpt = userService.findByUsername(principal.getName());
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userOpt.get();
        // Don't return the password in the response
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> currentUserOpt = userService.findByUsername(principal.getName());
        if (!currentUserOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User currentUser = currentUserOpt.get();

        // Only allow users to update their own accounts
        if (!user.getId().equals(currentUser.getId())) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "You can only update your own account");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            User updatedUser = userService.update(user);
            // Don't return the password in the response
            updatedUser.setPassword(null);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}