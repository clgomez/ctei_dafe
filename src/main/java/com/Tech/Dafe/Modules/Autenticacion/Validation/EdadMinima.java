package com.Tech.Dafe.Modules.Autenticacion.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = { EdadMinimaValidator.class }) //AQUÍ ESTÁ LA CLAVE
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EdadMinima {

    String message() default "Debe tener al menos {min} años";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min();
}