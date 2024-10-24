package com.Tech.Dafe.Modules.Proyecto.Proyectos.Services;

import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Repository.Asignacion_rolesRepository;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoServiceImpl implements ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private Asignacion_rolesRepository asignacionRolesRepository;

    @Override
    public Proyecto save(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Override
    public Iterable<Proyecto> findAll() {
        return proyectoRepository.findAll();
    }

    @Override
    public Optional<Proyecto> findById(Long id) {
        return proyectoRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        asignacionRolesRepository.deleteById(id);
        proyectoRepository.deleteById(id);
    }

    @Override
    public List<Proyecto> obtenerProyectosPorUsuario(Long usuarioId) {
        return proyectoRepository.findByUsuarioId(usuarioId);
    }
}
