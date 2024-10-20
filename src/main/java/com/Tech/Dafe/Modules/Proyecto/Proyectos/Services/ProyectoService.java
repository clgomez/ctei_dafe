package com.Tech.Dafe.Modules.Proyecto.Proyectos.Services;

import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;

import java.util.Optional;

public interface ProyectoService {

    Proyecto save(Proyecto proyecto);
    public Iterable<Proyecto> findAll();
    public Optional<Proyecto> findById(Long id);
    public void deleteById(Long id);

}
