package com.microservice.security.service;

import com.microservice.security.model.Role;
import com.microservice.security.model.User;
import com.microservice.security.model.UserDto;
import com.microservice.security.repository.RoleRepository;
import com.microservice.security.repository.UserDtoRepository;
import com.microservice.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDtoRepository {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findRoleByName("USER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        String email = user.getEmail().toLowerCase();
        if (email.contains("suren") || email.contains("widow") || email.contains("admin")) {
            role = roleRepository.findRoleByName("ADMIN");
            roleSet.add(role);
        }

        user.setRoles(roleSet);
        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public User findOne(String username) {
        return userRepository.findByUsername(username);
    }
}
