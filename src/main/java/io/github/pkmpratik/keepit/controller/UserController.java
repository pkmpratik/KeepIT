package io.github.pkmpratik.keepit.controller;

import io.github.pkmpratik.keepit.entity.User;
import io.github.pkmpratik.keepit.repository.UserRepository;
import io.github.pkmpratik.keepit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<java.lang.String> createUser(@RequestBody User user) {
        if (user == null) {
            return ResponseEntity.badRequest().body("User cannot be null");
        } else if (user.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Username cannot be empty");
        } else if (user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be empty");
        }else if (user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email cannot be empty");
        }
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created with Username " + createdUser.getUsername());
    }

    @PutMapping("/update_user")
    public ResponseEntity<java.lang.String> updateUser(@RequestBody Map<String, String> user) {
        if (user == null) {
            return ResponseEntity.badRequest().body("User cannot be null");
        }
        boolean isUpdated = userService.updateUser(user);
        return isUpdated ? ResponseEntity.ok("User Updated") : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete_user")
    public ResponseEntity<?> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/user_details")
    public ResponseEntity<?> userDetails() {
        User user = userService.getUserDetails();
        return ResponseEntity.ok(user);
    }
}
