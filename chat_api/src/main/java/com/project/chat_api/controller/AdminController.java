package com.project.chat_api.controller;
import com.project.chat_api.models.Role;
import com.project.chat_api.models.RoleName;
import com.project.chat_api.models.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.project.chat_api.repos.UserRepository;
import java.util.ArrayList;
import com.project.chat_api.payload.request.DeleteRequest;
import com.project.chat_api.payload.request.LoginRequest;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @CrossOrigin(origins = "*")
    @PostMapping("/deleteuser")
    public ResponseEntity<?> deleteuser(@Valid @RequestBody DeleteRequest deleteRequest) {
        Long id = deleteRequest.getId();

        User user = userRepository.findUserById(id);
        if (user == null) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }

        for (Role role : user.getRoles()) {
            if (RoleName.ADMIN.equals(role.getRoleName())) {
                return ResponseEntity.badRequest().body("Вы пытаетесь удалить пользователя с правами администратора");
            }
        }
        userRepository.delete(user);

        return ResponseEntity.ok("Пользователь успешно удален");
    }
    @CrossOrigin(origins = "*")
    @GetMapping( "/getallusers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<User> filteredUsers = new ArrayList<>();
        for (User user : users) {
            User filteredUser = new User(user.getId(), user.getEmail(), user.getUsername(), user.getRoles());
            filteredUsers.add(filteredUser);
        }

        return ResponseEntity.ok(filteredUsers);
    }
    @CrossOrigin(origins = "*")
    @GetMapping( "/test")

    public ResponseEntity<?> test()
    {
        return ResponseEntity.ok("done");
    }



}
