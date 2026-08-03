package com.Tech.Dafe.Modules.Proyecto.Proyectos.Export;

import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Response.CampoError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public final class DateParser {

    private DateParser(){}

    public static LocalDateTime parse(String fecha){

        List<DateTimeFormatter> formatos=List.of(

                DateTimeFormatter.ISO_LOCAL_DATE_TIME,

                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),

                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        );

        for(DateTimeFormatter formatter:formatos){

            try{

                return LocalDateTime.parse(fecha.trim(),formatter);

            }

            catch (DateTimeParseException ignored){}

        }

        throw new BusinessException(

                List.of(

                        new CampoError(
                                "fecha",
                                "Formato de fecha inválido: "+fecha
                        )

                )

        );

    }

}