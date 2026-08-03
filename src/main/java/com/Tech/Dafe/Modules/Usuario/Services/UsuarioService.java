package com.Tech.Dafe.Modules.Usuario.Services;

import com.Tech.Dafe.Modules.Autenticacion.Modelos.Rol;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;
import com.Tech.Dafe.Modules.Usuario.DTO.UsuarioUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    public Iterable<Usuario> findAll();
    public Optional<Usuario> findById(Long id);
    public Usuario save(Usuario usuario);
    public void deleteById(Long id);
    Optional<Rol> findRolByNombre(RolNombre rolNombre);
    List<Usuario> findByRoles_RolNombre(RolNombre rolNombre);
    Optional<Usuario> obtenerUsuarioPorIdentificacion(String identificacion);
    Usuario actualizarUsuario(Long id, UsuarioUpdateDTO dto);
    public Usuario obtenerPorId(Long id);
    public void eliminarUsuario(Long id);
    public Optional<Usuario> obtenerUsuarioPorEmail(String email);
    //public Optional<Usuario> obtenerUsuarioEvaluadorPorProyectoIdYUsuarioInvestigadorIdYUsuarioTutorId(Long proyectoId, Long usuarioInvestigadorId, Long usuarioTutorId);
    public Usuario obtenerUsuarioEvaluadorPorProyectoIdYUsuarioInvestigadorIdYUsuarioTutorId(Long proyectoId, Long usuarioInvestigadorId, Long usuarioTutorId);
    //public Optional<Usuario> obtenerUsuarioTutorPorProyectoIdYUsuarioInvestigadorIdYUsuarioEvaluadorId(Long proyectoId, Long usuarioInvestigadorId, Long usuarioEvaluadorId);
    public Usuario obtenerUsuarioTutorPorProyectoIdYUsuarioInvestigadorIdYUsuarioEvaluadorId(Long proyectoId, Long usuarioInvestigadorId, Long usuarioEvaluadorId);
    
}
