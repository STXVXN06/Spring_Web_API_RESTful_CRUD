package com.steven.curso.springboot.app.springboot_crud.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.steven.curso.springboot.app.springboot_crud.services.UserServiceImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername,String> {

    @Autowired
    private UserServiceImpl service;
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
    
        return !service.existsByUsername(username);
    }

    

}
