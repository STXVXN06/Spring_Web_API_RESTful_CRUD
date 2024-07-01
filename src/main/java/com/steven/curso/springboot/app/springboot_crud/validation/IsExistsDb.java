package com.steven.curso.springboot.app.springboot_crud.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = IsExistsDbValidation.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsExistsDb {

    String message() default "Ya existe en la base de datos!!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
