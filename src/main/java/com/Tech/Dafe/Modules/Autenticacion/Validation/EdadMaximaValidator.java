package com.Tech.Dafe.Modules.Autenticacion.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class EdadMaximaValidator implements ConstraintValidator<EdadMaxima, LocalDate> {

    private int edadMaxima;

    @Override
    public void initialize(EdadMaxima constraintAnnotation) {
        this.edadMaxima = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(LocalDate fechaNacimiento, ConstraintValidatorContext context) {

        if (fechaNacimiento == null) {
            return true;
        }

        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

        return edad <= edadMaxima;
    }
}