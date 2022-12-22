package com.microservice.security.repository;

import com.microservice.security.model.User;
import com.microservice.security.model.UserDto;

import java.util.List;

public interface UserDtoRepository {
    void save(UserDto user);
    List<User> findAll();
    User findOne(String username);
}
