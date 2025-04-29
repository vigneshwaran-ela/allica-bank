package com.allica.allica_bank.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.allica.allica_bank.validator.LoginNameValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Custom annotation to validate that the login name is unique.
 * 
 */
@Constraint(validatedBy = LoginNameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueLoginName {

    /**
     * Default validation message.
     */
	String message() default "Login name already in use, please try with some other name";

	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default{};
}
