package io.github.pkmpratik.keepit.service;

import io.github.pkmpratik.keepit.entity.User;
import io.github.pkmpratik.keepit.exception.EmailAlreadyExist;
import io.github.pkmpratik.keepit.exception.UsernameAlreadyExist;
import io.github.pkmpratik.keepit.repository.NoteRepository;
import io.github.pkmpratik.keepit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final NoteRepository noteRepository;

    @Autowired
    public UserService(UserRepository userRepository, NoteRepository noteRepository) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    public User createUser(User user) {
        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.insert(user);
    }

    public boolean updateUser(Map<String, String> updateDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> userinDB = userRepository.findByUsername(currentUsername);
        if (userinDB.isPresent()) {
            User existingUser = userinDB.get();

            if (updateDetails.containsKey("username")) {
                String newUsername = updateDetails.get("username");
                if (existingUser.getUsername() != newUsername) {
                    validateUsername(newUsername);
                    existingUser.setUsername(newUsername);
                }else existingUser.setUsername(currentUsername);
            }

            if (updateDetails.containsKey("email")) {
                String newEmail = updateDetails.get("email");
                if (existingUser.getEmail() != newEmail) {
                    validateEmail(newEmail);
                    existingUser.setEmail(newEmail);
                }
            }

            if (updateDetails.containsKey("password")) {
                String newPassword = updateDetails.get("password");
                if (newPassword != null && !newPassword.trim().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(newPassword));
                }
            }
            userRepository.save(existingUser);
            return true;
        }
        return false;
    }

    public void deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        noteRepository.deleteByUsername(username);
        userRepository.deleteByUsername(username);
    }


    private void validateUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExist(username + " Already Exist Try With Unique Username");
        }
    }

    private void validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExist(email + " Already Exist Try With another Email");
        }
    }

    public User getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return (User) userRepository.findByUsername(username).get();
    }
}
