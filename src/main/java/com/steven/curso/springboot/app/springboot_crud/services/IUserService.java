package com.steven.curso.springboot.app.springboot_crud.services;

import java.util.List;

import com.steven.curso.springboot.app.springboot_crud.entities.User;

public interface IUserService {

    List<User> findAll();

    User save(User user);
}
