package com.alexsitiy.script.evaluation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = ScriptValidator.class)
public @interface CheckScript {
    String message() default "Script has Syntax errors.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
