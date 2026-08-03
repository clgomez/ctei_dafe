package com.Tech.Dafe.Modules.Autenticacion.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = { EdadMaximaValidator.class })
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EdadMaxima {

    String message() default "La edad no puede ser mayor a {max} años";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int max();

}