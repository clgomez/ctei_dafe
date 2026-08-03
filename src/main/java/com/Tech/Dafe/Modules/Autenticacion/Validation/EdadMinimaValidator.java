package com.Tech.Dafe.Modules.Autenticacion.Validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class EdadMinimaValidator implements ConstraintValidator<EdadMinima, LocalDate> {

    private int edadMinima;

    @Override
    public void initialize(EdadMinima constraintAnnotation) {
        this.edadMinima = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate fechaNacimiento, ConstraintValidatorContext context) {

        if (fechaNacimiento == null) {
            return true;
        }

        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        return edad >= edadMinima;
    }
}