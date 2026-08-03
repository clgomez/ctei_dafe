package com.Tech.Dafe.Modules.Usuario.Services;

import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Actividades.Actividades.Models.Actividad;
import com.Tech.Dafe.Modules.Actividades.Actividades.Repository.ActividadRepository;
import com.Tech.Dafe.Modules.Actividades.Cronograma.Repository.CronogramaRepository;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Repository.ArbolDeObjetivosRepository;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fin.Repository.FinRepository;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Repository.MedioRepository;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Repository.ArbolDeProblemasRepository;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Repository.CausaRepository;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Repository.EfectoRepository;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Rol;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Autenticacion.Repositorio.RolRepository;
import com.Tech.Dafe.Modules.Calificaciones.Models.Calificacion;
import com.Tech.Dafe.Modules.Calificaciones.Repository.CalificacionRepository;
import com.Tech.Dafe.Modules.Calificaciones.Repository.EvaluacionDetalleRepository;
import com.Tech.Dafe.Modules.Convocatoria.Inscripción.Repository.InscripcionRepository;
import com.Tech.Dafe.Modules.Enums.Roles.Enums.RolNombre;
import com.Tech.Dafe.Modules.Notificaciones.Repository.NotificacionRepository;
import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.DTO.AsignacionDeRolesDTO;
import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Repository.AsignacionDeRolesRepository;
import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Services.AsignacionDeRolesService;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import com.Tech.Dafe.Modules.Usuario.DTO.UsuarioUpdateDTO;
import com.Tech.Dafe.Modules.Usuario.Repositorio.UsuarioRepository;
import com.Tech.Dafe.Response.CampoError;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final RolRepository rolRepository;

    private final PasswordEncoder passwordEncoder;

    private final NotificacionRepository notificacionRepository;  

    private final AsignacionDeRolesRepository asignacionDeRolesRepository;

    private final AsignacionDeRolesService asignacionDeRolesService;

    private final InscripcionRepository inscripcionRepository;

    private final CalificacionRepository calificacionRepository;

    private final EvaluacionDetalleRepository evaluacionDetalleRepository;
    
    private final ProyectoRepository proyectoRepository;

    private final ActividadRepository actividadRepository;

    private final CronogramaRepository cronogramaRepository;

    private final ArbolDeProblemasRepository arbolDeProblemasRepository;

    private final CausaRepository causaRepository;

    private final EfectoRepository efectoRepository;

    private final ArbolDeObjetivosRepository arbolDeObjetivosRepository;

    private final MedioRepository medioRepository;

    private final FinRepository finRepository;

    
    @Override
    public Iterable<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Rol> findRolByNombre(RolNombre rolNombre) {
        return rolRepository.findByRolNombre(rolNombre);
    }

    @Override
    public List<Usuario> findByRoles_RolNombre(RolNombre rolNombre) {
        return usuarioRepository.findByRoles_RolNombre(rolNombre);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorIdentificacion(String identificacionUsuario) {
       return usuarioRepository.findByIdentificacion(identificacionUsuario);
    }

    @Override
    public Usuario actualizarUsuario(Long id, UsuarioUpdateDTO dto) {

        Usuario usuarioDb = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        
        List<CampoError> errores = new ArrayList<>();

        // ==========================
        // VALIDACIONES DE UNICIDAD
        // ==========================

        if (usuarioRepository.existsByUsernameAndIdNot(dto.getUsername(), id)) {
            errores.add(new CampoError("username", "El nombre de usuario ya está en uso"));
        }

        if (usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            errores.add(new CampoError("email", "El email ya está en uso"));
        }

        if (usuarioRepository.existsByIdentificacionAndIdNot(dto.getIdentificacion(), id)) {
            errores.add(new CampoError("identificacion", "La identificación ya está registrada"));
        }

        if (usuarioRepository.existsByTelefonoAndIdNot(dto.getTelefono(), id)) {
            errores.add(new CampoError("telefono", "El teléfono ya está en uso"));
        }

        // Si hay errores → lanzar excepción
        if (!errores.isEmpty()) {
            throw new BusinessException(errores);
        }

        usuarioDb.setNombre(dto.getNombre());
        usuarioDb.setApellidos(dto.getApellidos());
        usuarioDb.setDireccion(dto.getDireccion());
        usuarioDb.setEmail(dto.getEmail());
        usuarioDb.setEstado(dto.getEstado());
        usuarioDb.setFechaNacimiento(dto.getFechaNacimiento());
        usuarioDb.setTipoIdentificacion(dto.getTipoIdentificacion());
        usuarioDb.setIdentificacion(dto.getIdentificacion());
        usuarioDb.setGenero(dto.getGenero());
        usuarioDb.setOcupacion(dto.getOcupacion());
        usuarioDb.setUsername(dto.getUsername());
        usuarioDb.setTelefono(dto.getTelefono());

        // Password opcional
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            usuarioDb.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Roles
        if (dto.getRoles() != null) {
            Set<Rol> roles = dto.getRoles().stream()
                    .map(rolNombre -> rolRepository.findByRolNombre(rolNombre)
                            .orElseThrow(() -> new ResourceNotFoundException("El rol " + rolNombre + " no existe")))
                    .collect(Collectors.toSet());

            usuarioDb.setRoles(roles);
        }

        return usuarioRepository.save(usuarioDb);
    }

    public Usuario obtenerPorId(Long id){
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

     // ==============================
    // ELIMINAR USUARIO
    // ==============================
    @Transactional
    @Override
    public void eliminarUsuario(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + usuarioId
                ));

        Set<RolNombre> roles = usuario.getRoles()
                .stream()
                .map(Rol::getRolNombre)
                .collect(Collectors.toSet());

        // ADMIN NO SE ELIMINA
        if (roles.contains(RolNombre.ROL_ADMINISTRADOR)) {
            throw new BusinessException(
                    List.of(new com.Tech.Dafe.Response.CampoError(
                            "rolnombre",
                            "No se puede eliminar un usuario con rol administrador"
                    ))
            );
        }

        // INVESTIGADOR
        if (roles.contains(RolNombre.ROL_INVESTIGADOR)) {
            eliminarComoInvestigador(usuarioId);
        } 
        // TUTOR / EVALUADOR
        else {
            eliminarComoTutorOEvaluador(usuarioId);
        }

        // ELIMINAR USUARIO
        usuarioRepository.delete(usuario);
    }

    // ==============================
    // INVESTIGADOR
    // ==============================
    private void eliminarComoInvestigador(Long usuarioId) {

        // Calificaciones
        List<Calificacion> calificaciones =
                calificacionRepository.findByUsuarioInvestigadorId(usuarioId);

        if (!calificaciones.isEmpty()) {
            calificacionRepository.deleteAll(calificaciones);
            calificacionRepository.flush();
        }

        // Inscripciones
        inscripcionRepository.deleteByUsuarioId(usuarioId);

        // Notificaciones
        notificacionRepository.deleteByUsuarioId(usuarioId);

        // Asignaciones
        asignacionDeRolesRepository.deleteByUsuarioInvestigadorId(usuarioId);
        asignacionDeRolesRepository.flush();

        // Proyectos
        List<Proyecto> proyectos = proyectoRepository.findByUsuarioId(usuarioId);

        for (Proyecto proyecto : proyectos) {

            // Actividades + Cronogramas
            for (Actividad actividad : proyecto.getActividades()) {
                cronogramaRepository.deleteAll(actividad.getCronogramas());
            }

            actividadRepository.deleteAll(proyecto.getActividades());

            Long proyectoId = proyecto.getId();

            // Árbol de Problemas
            arbolDeProblemasRepository.findByProyectoId(proyectoId)
                    .ifPresent(arbol -> {
                        causaRepository.deleteByArbolDeProblemasId(arbol.getId());
                        efectoRepository.deleteByArbolProblemasId(arbol.getId());
                        arbolDeProblemasRepository.delete(arbol);
                    });

            // Árbol de Objetivos
            arbolDeObjetivosRepository.findByProyectoId(proyectoId)
                    .ifPresent(arbol -> {
                        medioRepository.deleteByArbolDeObjetivosId(arbol.getId());
                        finRepository.deleteByArbolDeObjetivosId(arbol.getId());
                        arbolDeObjetivosRepository.delete(arbol);
                    });

            proyectoRepository.delete(proyecto);
        }
    }

    // ==============================
    // TUTOR / EVALUADOR
    // ==============================
    private void eliminarComoTutorOEvaluador(Long usuarioId) {

         Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Usuario no encontrado con ID: " + usuarioId
            ));

        Long idHistorico = usuario.getId();

        String nombreCompleto =
                usuario.getNombre() + " " + usuario.getApellidos();

        // Notificaciones (SI se eliminan)
        notificacionRepository.deleteByUsuarioId(usuarioId);

        // Asignaciones
        asignacionDeRolesRepository.desvincularUsuario(usuarioId);
      
       // Archivar evaluaciones del tutor/evaluador
         evaluacionDetalleRepository.guardarDatosHistoricos(
            usuarioId,
            usuarioId,
            nombreCompleto
        );

         // Desvincular evaluaciones del tutor/evaluador
        evaluacionDetalleRepository.desvincularUsuario(usuarioId);

        // Guardar histórico en calificacion del tutor
        calificacionRepository.guardarHistoricoTutor(
            usuarioId,
            idHistorico,
            nombreCompleto
        );

        // Guardar histórico en calificacion del evaluador
        calificacionRepository.guardarHistoricoEvaluador(
            usuarioId,
            idHistorico,
            nombreCompleto
        );

       // Desvincular tutor/evaluador
       calificacionRepository.desvincularUsuario(usuarioId);

    }


    @Override
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }


    /*@Override
    public Optional<Usuario> obtenerUsuarioTutorPorProyectoIdYUsuarioInvestigadorIdYUsuarioEvaluadorId(
            Long proyectoId, Long usuarioInvestigadorId, Long usuarioEvaluadorId) {

        Optional<AsignacionDeRolesDTO> asignacionOpt =
            asignacionDeRolesService
                .obtenerAsignacionDeRolesPorIdProyectoYIdUsuarioInvestigadorYIdUsuarioEvaluador(
                    proyectoId, usuarioInvestigadorId, usuarioEvaluadorId
                );

        if (asignacionOpt.isEmpty()) {
            return Optional.empty();
        }

        Long tutorId = asignacionOpt.get().getUsuarioTutorId();

        if (tutorId == null) {
            return Optional.empty();
        }

        return usuarioRepository.findById(tutorId);
    }*/

    @Override
    public Usuario obtenerUsuarioTutorPorProyectoIdYUsuarioInvestigadorIdYUsuarioEvaluadorId(
            Long proyectoId,
            Long usuarioInvestigadorId,
            Long usuarioEvaluadorId) {

        AsignacionDeRolesDTO asignacion =
                asignacionDeRolesService
                        .obtenerAsignacionDeRolesPorIdProyectoYIdUsuarioInvestigadorYIdUsuarioEvaluador(
                                proyectoId,
                                usuarioInvestigadorId,
                                usuarioEvaluadorId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No existe una asignación para los parámetros suministrados"));

        Long tutorId = asignacion.getUsuarioTutorId();

        if (tutorId == null) {
            throw new ResourceNotFoundException(
                    "La asignación no tiene tutor asociado");
        }

        return usuarioRepository.findById(tutorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tutor no encontrado con ID: " + tutorId));
    }     




   /*@Override
    public Optional<Usuario> obtenerUsuarioEvaluadorPorProyectoIdYUsuarioInvestigadorIdYUsuarioTutorId(
        Long proyectoId, Long usuarioInvestigadorId, Long usuarioTutorId
    ) {

    Optional<AsignacionDeRolesDTO> asignacionOpt =
            asignacionDeRolesService
                .obtenerAsignacionDeRolesPorIdProyectoYIdUsuarioInvestigadorYIdUsuarioTutor(
                    proyectoId, usuarioInvestigadorId, usuarioTutorId
                );

        if (asignacionOpt.isEmpty()) {
            return Optional.empty();
        }

        Long evaluadorId = asignacionOpt.get().getUsuarioEvaluadorId();

        if (evaluadorId == null) {
            return Optional.empty();
        }

        return usuarioRepository.findById(evaluadorId);
    }
    */

   @Override
    public Usuario obtenerUsuarioEvaluadorPorProyectoIdYUsuarioInvestigadorIdYUsuarioTutorId(
            Long proyectoId,
            Long usuarioInvestigadorId,
            Long usuarioTutorId) {

        AsignacionDeRolesDTO asignacion =
                asignacionDeRolesService
                        .obtenerAsignacionDeRolesPorIdProyectoYIdUsuarioInvestigadorYIdUsuarioTutor(
                                proyectoId,
                                usuarioInvestigadorId,
                                usuarioTutorId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No existe una asignación para los parámetros suministrados"));

        Long evaluadorId = asignacion.getUsuarioEvaluadorId();

        if (evaluadorId == null) {
            throw new ResourceNotFoundException(
                    "La asignación no tiene evaluador asociado");
        }

        return usuarioRepository.findById(evaluadorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Evaluador no encontrado con ID: " + evaluadorId));
    }
   
}
