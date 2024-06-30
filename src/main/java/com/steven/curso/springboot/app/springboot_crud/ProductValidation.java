package com.steven.curso.springboot.app.springboot_crud;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.steven.curso.springboot.app.springboot_crud.entities.Product;

@Component
public class ProductValidation implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", null, "es requerido!! VALIDATION");

        if (product.getDescription() == null || product.getDescription().isBlank()) {
            errors.rejectValue("description", null, "es requerido, por favor! VALIDATION");
        }

        if (product.getPrice() == null ) {
            errors.rejectValue("price", null, "no puede ser nulo, ok? VALIDATION");
        }else if( product.getPrice() < 500){
            errors.rejectValue("price", null, "debe ser numero mayor o igual queeeeeee 500!! VALIDATION");

        }
    }

}
