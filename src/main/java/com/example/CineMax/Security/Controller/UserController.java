package com.example.CineMax.Security.Controller;

import com.example.CineMax.Security.DTO.LoginRequest;
import com.example.CineMax.Security.DTO.LoginResponse;
import com.example.CineMax.Security.DTO.RegistrationRequest;
import com.example.CineMax.Security.Entity.Role;
import com.example.CineMax.Security.Entity.User;
import com.example.CineMax.Security.Repository.RoleRepository;
import com.example.CineMax.Security.Repository.UserRepository;
import com.example.CineMax.Security.Service.CustomUserDetailsService;
import com.example.CineMax.Security.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = (User) userService.loadUserByUsername(loginRequest.getUsername());

        if (userService.checkPassword(user, loginRequest.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(new LoginResponse(token));
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());

        Set<Role> roles = new HashSet<>();
        if (registrationRequest.isAdmin()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            if (adminRole != null) {
                roles.add(adminRole);
            }
        } else {
            Role userRole = roleRepository.findByName("ROLE_USER");
            if (userRole != null) {
                roles.add(userRole);
            }
        }
        user.setRoles(roles);

        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}
