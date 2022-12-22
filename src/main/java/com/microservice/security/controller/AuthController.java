package com.microservice.security.controller;

import com.microservice.security.config.jwtUtils.JwtTokenProvider;
import com.microservice.security.model.*;
import com.microservice.security.repository.RoleRepository;
import com.microservice.security.repository.UserRepository;
import com.microservice.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping(value = "/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginUser loginUser) {
        Authentication authentication;
        final String token;
        ResponseEntity<?> tokenResponseEntity;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenProvider.generateToken(authentication);
            tokenResponseEntity = new ResponseEntity<>(new JsonToken(token), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            tokenResponseEntity = new ResponseEntity<>(new ResponseMessage("Invalid username or password."), HttpStatus.NOT_FOUND);
        }

        return tokenResponseEntity;
    }

    @PostMapping("/user/add")
    public ResponseEntity<?> saveUser(@RequestBody UserDto user) {
        userService.save(user);
        return new ResponseEntity<>(new ResponseMessage("New user created for " + user.getUsername()), HttpStatus.CREATED);
    }

    @PostMapping("/role/add")
    public ResponseEntity<?> saveUser(@RequestBody Role role) {
        roleRepository.save(role);
        return new ResponseEntity<>(new ResponseMessage("Role added " + role.getName()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/admin")
    public ResponseEntity<?> admin() {
        return new ResponseEntity<>(new ResponseMessage("You have permission to access this api ADMIN !"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/user")
    public ResponseEntity<?> user() {
        return new ResponseEntity<>(new ResponseMessage("You have permission to access this api USER !"), HttpStatus.OK);
    }
}
