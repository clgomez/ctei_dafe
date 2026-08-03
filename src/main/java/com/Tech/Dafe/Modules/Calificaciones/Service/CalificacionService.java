package com.Tech.Dafe.Modules.Calificaciones.Service;

import com.Tech.Dafe.Modules.Calificaciones.Repository.CalificacionRepository;
import com.Tech.Dafe.Modules.Enums.Calificaciones.Enums.EstadoCalificacion;
import com.Tech.Dafe.Modules.Enums.Calificaciones.Enums.RolCalificador;
import com.Tech.Dafe.Modules.Enums.Semaforo.Enum.SemaforoEnum;
import com.Tech.Dafe.Modules.Notificaciones.Services.NotificacionService;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Calificaciones.DTO.CalificacionDTO;
import com.Tech.Dafe.Modules.Calificaciones.DTO.EvaluacionDetalleDTO;
import com.Tech.Dafe.Modules.Calificaciones.DTO.RubroCalificacionDTO;
import com.Tech.Dafe.Modules.Calificaciones.DTO.RubroEvaluacionDTO;
import com.Tech.Dafe.Modules.Calificaciones.Models.Calificacion;
import com.Tech.Dafe.Modules.Calificaciones.Models.EvaluacionDetalle;
import com.Tech.Dafe.Modules.Calificaciones.Models.RubroCalificacion;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import com.Tech.Dafe.Modules.Usuario.Repositorio.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalificacionService {
    
    private final ProyectoRepository proyectoRepository;

    private final CalificacionRepository calificacionRepository;
    
    private final UsuarioRepository usuarioRepository;
    
    private final NotificacionService notificacionService;

    private void asignarEstadoYSemaforo(Calificacion calificacion, float notaFinal) {

        if (notaFinal <= 33.3) {
            calificacion.setSemaforo(SemaforoEnum.Rojo);
            calificacion.setEstado(EstadoCalificacion.NO_APROBADO);
        } else if (notaFinal <= 66.6) {
            calificacion.setSemaforo(SemaforoEnum.Amarillo);
            calificacion.setEstado(EstadoCalificacion.POR_MEJORAR);
        } else {
            calificacion.setSemaforo(SemaforoEnum.Verde);
            calificacion.setEstado(EstadoCalificacion.APROBADO);
        }
    }

private Float calcularPromedioPorRol(Calificacion calificacion, RolCalificador rol) {

        List<Float> notas = calificacion.getRubros().stream()
                .flatMap(r -> r.getEvaluaciones().stream())
                .filter(ev -> ev.getRolCalificador() == rol)
                .map(EvaluacionDetalle::getNota)
                .filter(n -> n != null)
                .toList();

        if (notas.isEmpty()) {
        return null;
        }

        return (float) notas.stream()
                .mapToDouble(Float::doubleValue)
                .average()
                .orElse(0);
}

private void enviarNotificacionCalificacion(
        Calificacion calificacion,
        RolCalificador rol) {

    Proyecto proyecto = calificacion.getProyecto();

    Usuario usuarioCalificador;
    Float notaParcial;

    if (rol == RolCalificador.TUTOR) {
        usuarioCalificador = calificacion.getUsuarioTutor();
        notaParcial = calificacion.getNotaTutor();
    } else {
        usuarioCalificador = calificacion.getUsuarioEvaluador();
        notaParcial = calificacion.getNotaEvaluador();
    }

    if (usuarioCalificador == null) {
        return;
    }

    String notaTexto = notaParcial != null
        ? String.format("%.1f%%", notaParcial)
        : "sin nota registrada";        

    String mensaje =
            "El "
            + rol.name().toLowerCase()
            + " "
            + usuarioCalificador.getNombre()
            + " "
            + usuarioCalificador.getApellidos()
            + " ha calificado el proyecto "
            + proyecto.getTitulo()
            + " con una nota parcial de "
            + notaTexto;

    notificacionService.enviarNotificacionesAUsuario(
            proyecto.getUsuario().getId(),
            mensaje
    );
}

private boolean calificoRol(CalificacionDTO dto, RolCalificador rol) {
    return dto.getRubros() != null &&
            dto.getRubros().stream()
                    .flatMap(r -> r.getEvaluaciones().stream())
                    .anyMatch(ev -> ev.getRolCalificador() == rol);
}

@Transactional
public CalificacionDTO crearCalificacion(CalificacionDTO dto) {

    Proyecto proyecto = proyectoRepository.findById(dto.getProyectoId())
            .orElseThrow(() ->
                    new IllegalArgumentException("Proyecto no encontrado"));

    // =====================================================
    // BUSCAR SI YA EXISTE CALIFICACION
    // =====================================================

    Calificacion calificacion = calificacionRepository
            .findByProyectoId(proyecto.getId())
            .orElse(null);

  // =====================================================
    // CREAR SI NO EXISTE
    // =====================================================

    if (calificacion == null) {

        calificacion = new Calificacion();

        calificacion.setProyecto(proyecto);

        calificacion.setUsuarioInvestigador(
                proyecto.getUsuario());

        calificacion.setRubros(new ArrayList<>());
    }

    // =====================================================
    // ASIGNAR TUTOR
    // =====================================================

   if (dto.getTutorId() != null) {

    if (calificacion.getUsuarioTutor() == null) {

        Usuario tutor = usuarioRepository.findById(dto.getTutorId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Tutor no encontrado"));

        calificacion.setUsuarioTutor(tutor);

    } else if (!calificacion.getUsuarioTutor().getId()
            .equals(dto.getTutorId())) {

        throw new IllegalArgumentException(
                "La calificación ya tiene otro tutor asignado");
    }
  }

    // =====================================================
    // ASIGNAR EVALUADOR
    // =====================================================

   if (dto.getEvaluadorId() != null) {

    if (calificacion.getUsuarioEvaluador() == null) {

        Usuario evaluador = usuarioRepository.findById(dto.getEvaluadorId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Evaluador no encontrado"));

        calificacion.setUsuarioEvaluador(evaluador);

    } else if (!calificacion.getUsuarioEvaluador().getId()
            .equals(dto.getEvaluadorId())) {

        throw new IllegalArgumentException(
                "La calificación ya tiene otro evaluador asignado");
    }
  }

    // =====================================================
    // MAPA DE RUBROS EXISTENTES
    // =====================================================

    Map<String, RubroCalificacion> rubrosExistentes =
            calificacion.getRubros().stream()
                    .collect(Collectors.toMap(
                            r -> r.getTipoCalificacion() + "_" + r.getReferenciaId(),
                            r -> r
                    ));

    // =====================================================
    // RECORRER RUBROS DEL DTO
    // =====================================================

    for (RubroCalificacionDTO rubroDTO : dto.getRubros()) {

        String key =
                rubroDTO.getTipoCalificacion() + "_" + rubroDTO.getReferenciaId();

        RubroCalificacion rubro =
                rubrosExistentes.get(key);

        // =================================================
        // SI EL RUBRO NO EXISTE -> CREAR
        // =================================================

        if (rubro == null) {

            rubro = new RubroCalificacion();

            rubro.setCalificacion(calificacion);

            rubro.setTipoCalificacion(
                    rubroDTO.getTipoCalificacion());

            rubro.setReferenciaId(
                    rubroDTO.getReferenciaId());

            rubro.setDescripcion(
                    rubroDTO.getDescripcion());

            rubro.setEvaluaciones(new ArrayList<>());

            calificacion.getRubros().add(rubro);
        }

        // =================================================
        // AGREGAR EVALUACIONES
        // =================================================

        for (EvaluacionDetalleDTO evalDTO :
                rubroDTO.getEvaluaciones()) {

            Usuario usuario = usuarioRepository
                    .findById(evalDTO.getUsuarioId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Usuario no encontrado"));

                // =====================================================
                // VALIDAR TUTOR
                // =====================================================

               if (evalDTO.getRolCalificador() == RolCalificador.TUTOR) {

                        if (calificacion.getUsuarioTutor() == null) {
                                throw new IllegalArgumentException(
                                        "No hay tutor asignado a la calificación");
                        }

                        if (!calificacion.getUsuarioTutor().getId()
                                .equals(usuario.getId())) {

                                throw new IllegalArgumentException(
                                        "Solo el tutor asignado puede crear evaluaciones de tutor");
                        }
                }

                // =====================================================
                // VALIDAR EVALUADOR
                // =====================================================

              if (evalDTO.getRolCalificador() == RolCalificador.EVALUADOR) {

                if (calificacion.getUsuarioEvaluador() == null) {
                        throw new IllegalArgumentException(
                                "No hay evaluador asignado a la calificación");
                }

                if (!calificacion.getUsuarioEvaluador().getId()
                        .equals(usuario.getId())) {

                        throw new IllegalArgumentException(
                                "Solo el evaluador asignado puede crear evaluaciones de evaluador");
                }
              }

            // VALIDAR SI YA EXISTE EVALUACION
            boolean yaExiste =
                    rubro.getEvaluaciones().stream()
                            .anyMatch(ev ->
                                    ev.getUsuario().getId()
                                            .equals(usuario.getId())
                                    &&
                                    ev.getRolCalificador()
                                            .equals(evalDTO.getRolCalificador())
                            );

            if (yaExiste) {
                continue;
            }

            EvaluacionDetalle evaluacion =
                    new EvaluacionDetalle();

            evaluacion.setRubro(rubro);

            evaluacion.setUsuario(usuario);

            evaluacion.setRolCalificador(evalDTO.getRolCalificador());

            evaluacion.setNota(evalDTO.getNota());

            evaluacion.setComentario(
                    evalDTO.getComentario());

            evaluacion.setActivo(
                    evalDTO.getActivo() != null
                            ? evalDTO.getActivo()
                            : true
            );

            rubro.getEvaluaciones().add(evaluacion);
        }
    }

  // =====================================================
// CALCULAR NOTA TUTOR
// =====================================================

Float notaTutor =
        calcularPromedioPorRol(
                calificacion,
                RolCalificador.TUTOR
        );

calificacion.setNotaTutor(notaTutor);

// =====================================================
// CALCULAR NOTA EVALUADOR
// =====================================================

Float notaEvaluador =
        calcularPromedioPorRol(
                calificacion,
                RolCalificador.EVALUADOR
        );

calificacion.setNotaEvaluador(notaEvaluador);

// =====================================================
// CALCULAR FINAL SOLO SI EXISTEN AMBAS
// =====================================================

if (notaTutor != null &&
        notaEvaluador != null) {

    float notaFinal =
            (notaTutor + notaEvaluador) / 2;

    calificacion.setCalificacionFinal(
            notaFinal);

    asignarEstadoYSemaforo(
            calificacion,
            notaFinal);

} else {

    // AUN EN PROCESO

    calificacion.setCalificacionFinal(null);

    calificacion.setEstado(null);

    calificacion.setSemaforo(SemaforoEnum.Pendiente);
}

    Calificacion guardada =
            calificacionRepository.save(calificacion);

        // =====================================================
        // NOTIFICACIONES
        // =====================================================
        if (calificoRol(dto, RolCalificador.TUTOR)) {
                enviarNotificacionCalificacion(guardada, RolCalificador.TUTOR);
        }

        if (calificoRol(dto, RolCalificador.EVALUADOR)) {
                enviarNotificacionCalificacion(guardada, RolCalificador.EVALUADOR);
        }

    return mapToDTO(guardada);
}
   
@Transactional
public CalificacionDTO actualizarCalificacion(
        Long id,
        CalificacionDTO dto) {

    Calificacion calificacion = calificacionRepository.findById(id)
            .orElseThrow(() ->
                    new IllegalArgumentException("Calificación no encontrada"));

    // =====================================================
    // MAPA RUBROS EXISTENTES
    // =====================================================

    Map<Long, RubroCalificacion> rubrosExistentes =
            calificacion.getRubros().stream()
                    .filter(r -> r.getId() != null)
                    .collect(Collectors.toMap(
                            RubroCalificacion::getId,
                            r -> r
                    ));

    // =====================================================
    // RECORRER RUBROS DTO
    // =====================================================

    for (RubroCalificacionDTO rubroDTO : dto.getRubros()) {

        RubroCalificacion rubro;

        // =================================================
        // RUBRO EXISTENTE
        // =================================================

        if (rubroDTO.getId() != null &&
                rubrosExistentes.containsKey(rubroDTO.getId())) {

            rubro = rubrosExistentes.get(rubroDTO.getId());

        } else {

            // NUEVO RUBRO

            rubro = new RubroCalificacion();

            rubro.setCalificacion(calificacion);

            rubro.setEvaluaciones(new ArrayList<>());

            calificacion.getRubros().add(rubro);
        }

        // =================================================
        // ACTUALIZAR DATOS RUBRO
        // =================================================

        rubro.setTipoCalificacion(
                rubroDTO.getTipoCalificacion());

        rubro.setReferenciaId(
                rubroDTO.getReferenciaId());

        rubro.setDescripcion(
                rubroDTO.getDescripcion());

        // =================================================
        // MAPA EVALUACIONES EXISTENTES
        // =================================================

        /*Map<Long, EvaluacionDetalle> evaluacionesExistentes =
                rubro.getEvaluaciones().stream()
                        .filter(e -> e.getId() != null)
                        .collect(Collectors.toMap(
                                EvaluacionDetalle::getId,
                                e -> e
                        ));*/

        // =================================================
        // RECORRER EVALUACIONES DTO
        // =================================================

        for (EvaluacionDetalleDTO evalDTO :
                rubroDTO.getEvaluaciones()) {

           

            // =============================================
            // EVALUACION EXISTENTE
            // =============================================

            /*if (evalDTO.getId() != null &&
                    evaluacionesExistentes.containsKey(evalDTO.getId())) {

                evaluacion =
                        evaluacionesExistentes.get(evalDTO.getId());

            } else {

                // NUEVA EVALUACION

                evaluacion = new EvaluacionDetalle();

                evaluacion.setRubro(rubro);

                rubro.getEvaluaciones().add(evaluacion);
            }*/

           EvaluacionDetalle evaluacion =
                rubro.getEvaluaciones().stream()
                        .filter(ev ->
                                ev.getUsuario() != null
                                &&
                                ev.getUsuario().getId().equals(evalDTO.getUsuarioId())
                                &&
                                ev.getRolCalificador() == evalDTO.getRolCalificador()
                        )
                        .findFirst()
                        .orElse(null);

                        if (evaluacion == null) {

                                evaluacion = new EvaluacionDetalle();

                                evaluacion.setRubro(rubro);

                                rubro.getEvaluaciones().add(evaluacion);
                        }    

            Usuario usuario = usuarioRepository
                    .findById(evalDTO.getUsuarioId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Usuario no encontrado"));

            // =====================================================
            // VALIDAR TUTOR
            // =====================================================

            if (evalDTO.getRolCalificador() == RolCalificador.TUTOR) {

                if (calificacion.getUsuarioTutor() == null) {
                        throw new IllegalArgumentException(
                                "No hay tutor asignado a la calificación");
                }

                if (!calificacion.getUsuarioTutor().getId()
                        .equals(usuario.getId())) {

                        throw new IllegalArgumentException(
                                "Solo el tutor asignado puede crear evaluaciones de tutor");
                }
           }

            // =====================================================
            // VALIDAR EVALUADOR
            // =====================================================

            if (evalDTO.getRolCalificador() == RolCalificador.EVALUADOR) {

                if (calificacion.getUsuarioEvaluador() == null) {
                        throw new IllegalArgumentException(
                                "No hay evaluador asignado a la calificación");
                }

                if (!calificacion.getUsuarioEvaluador().getId()
                        .equals(usuario.getId())) {

                        throw new IllegalArgumentException(
                                "Solo el evaluador asignado puede crear evaluaciones de evaluador");
                }
            }

            // =====================================================
            // ACTUALIZAR CAMPOS
            // =====================================================

            evaluacion.setUsuario(usuario);

            evaluacion.setRolCalificador(evalDTO.getRolCalificador());

            evaluacion.setNota(evalDTO.getNota());

            evaluacion.setComentario(evalDTO.getComentario());

            evaluacion.setActivo(
                    evalDTO.getActivo() != null
                            ? evalDTO.getActivo()
                            : true
            );
        }
    }

    // =====================================================
    // RECALCULAR NOTAS
    // =====================================================

    Float notaTutor =
            calcularPromedioPorRol(
                    calificacion,
                    RolCalificador.TUTOR
            );

    calificacion.setNotaTutor(notaTutor);

    Float notaEvaluador =
            calcularPromedioPorRol(
                    calificacion,
                    RolCalificador.EVALUADOR
            );

    calificacion.setNotaEvaluador(notaEvaluador);

    // =====================================================
    // CALCULAR FINAL
    // =====================================================

    if (notaTutor != null &&
            notaEvaluador != null) {

        float notaFinal =
                (notaTutor + notaEvaluador) / 2;

        calificacion.setCalificacionFinal(notaFinal);

        asignarEstadoYSemaforo(
                calificacion,
                notaFinal);

    } else {

        calificacion.setCalificacionFinal(null);

        calificacion.setEstado(null);

        calificacion.setSemaforo(
                SemaforoEnum.Pendiente);
    }

    Calificacion actualizada =
            calificacionRepository.save(calificacion);

        // =====================================================
        // NOTIFICACIONES
        // =====================================================
        if (calificoRol(dto, RolCalificador.TUTOR)) {
                enviarNotificacionCalificacion(actualizada, RolCalificador.TUTOR);
        }

        if (calificoRol(dto, RolCalificador.EVALUADOR)) {
                enviarNotificacionCalificacion(actualizada, RolCalificador.EVALUADOR);
        }

    return mapToDTO(actualizada);
}


@Transactional(readOnly = true)
public CalificacionDTO obtenerCalificacionCompleta(Long id) {

    Calificacion calificacion = calificacionRepository.findById(id)
            .orElseThrow(() ->
                    new IllegalArgumentException("Calificación no encontrada"));

    return mapToDTO(calificacion);
}

private CalificacionDTO mapToDTO(Calificacion calificacion) {

    CalificacionDTO dto = new CalificacionDTO();

    dto.setId(calificacion.getId());

    dto.setProyectoId(
            calificacion.getProyecto().getId());

    if (calificacion.getUsuarioTutor() != null) {

        dto.setTutorId(
                calificacion.getUsuarioTutor().getId());
    }

    if (calificacion.getUsuarioEvaluador() != null) {

        dto.setEvaluadorId(
                calificacion.getUsuarioEvaluador().getId());
    }

    if (calificacion.getUsuarioInvestigador() != null) {

        dto.setInvestigadorId(
                calificacion.getUsuarioInvestigador().getId());
    }

    dto.setNotaTutor(
        calificacion.getNotaTutor());

    dto.setNotaEvaluador(
        calificacion.getNotaEvaluador());

    dto.setCalificacionFinal(
            calificacion.getCalificacionFinal());

    dto.setEstado(
            calificacion.getEstado());

    dto.setFechaCreacion(
            calificacion.getFechaCreacion());

    // HISTÓRICO
    dto.setIdTutorHistorico(
            calificacion.getIdTutorHistorico());

    dto.setIdEvaluadorHistorico(
            calificacion.getIdEvaluadorHistorico());

    dto.setNombreTutorHistorico(
            calificacion.getNombreTutorHistorico());

    dto.setNombreEvaluadorHistorico(
            calificacion.getNombreEvaluadorHistorico());        

    List<RubroCalificacionDTO> rubros =
            calificacion.getRubros().stream().map(rubro -> {

                RubroCalificacionDTO rubroDTO =
                        new RubroCalificacionDTO();

                rubroDTO.setId(rubro.getId());

                rubroDTO.setTipoCalificacion(
                        rubro.getTipoCalificacion());

                rubroDTO.setReferenciaId(
                        rubro.getReferenciaId());

                rubroDTO.setDescripcion(
                        rubro.getDescripcion());

                List<EvaluacionDetalleDTO> evaluaciones =
                        rubro.getEvaluaciones().stream().map(eval -> {

                        EvaluacionDetalleDTO evalDTO =
                                new EvaluacionDetalleDTO();

                        evalDTO.setId(
                                eval.getId());

                        evalDTO.setRubroId(
                                rubro.getId());

                        evalDTO.setCalificacionId(
                                calificacion.getId());

                        // Usuario actual o histórico
                        if (eval.getUsuario() != null) {

                                evalDTO.setUsuarioId(
                                        eval.getUsuario().getId());

                        } else {

                                evalDTO.setUsuarioId(
                                        eval.getIdUsuarioHistorico());
                        }

                        evalDTO.setIdUsuarioHistorico(
                                eval.getIdUsuarioHistorico());

                        evalDTO.setNombreUsuarioHistorico(
                                eval.getNombreUsuarioHistorico());

                        evalDTO.setRolCalificador(
                                eval.getRolCalificador());

                        evalDTO.setNota(
                                eval.getNota());

                        evalDTO.setComentario(
                                eval.getComentario());

                        evalDTO.setActivo(
                                eval.getActivo());

                        return evalDTO;

                        }).toList();

                rubroDTO.setEvaluaciones(evaluaciones);

                return rubroDTO;

            }).toList();

    dto.setRubros(rubros);

    return dto;
}



public List<CalificacionDTO> obtenerTodas() {

    return calificacionRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .toList();
}


@Transactional(readOnly = true)
public List<RubroEvaluacionDTO> obtenerRubricaProyecto(Long proyectoId) {

    Proyecto proyecto = proyectoRepository.findById(proyectoId)
            .orElseThrow(() ->
                    new IllegalArgumentException("Proyecto no encontrado"));

    List<RubroEvaluacionDTO> rubros = new ArrayList<>();

    // =========================================================
    // DATOS GENERALES DEL PROYECTO
    // =========================================================

    if (proyecto.getTitulo() != null) {

        rubros.add(
                RubroEvaluacionDTO.builder()
                        .tipo("TITULO")
                        .descripcion(proyecto.getTitulo())
                        .build()
        );
    }

    if (proyecto.getDescripcion() != null) {

        rubros.add(
                RubroEvaluacionDTO.builder()
                        .tipo("DESCRIPCION")
                        .descripcion(proyecto.getDescripcion())
                        .build()
        );
    }

    // =========================================================
    // ÁRBOL DE PROBLEMAS
    // =========================================================

    if (proyecto.getArbolDeProblemas() != null) {

        // CAUSAS
        if (proyecto.getArbolDeProblemas().getCausas() != null) {

            proyecto.getArbolDeProblemas()
                    .getCausas()
                    .forEach(causa -> {

                        rubros.add(
                                RubroEvaluacionDTO.builder()
                                        .tipo("CAUSA")
                                        .referenciaId(causa.getId())
                                        .descripcion(causa.getDescripcion())
                                        .build()
                        );
                    });
        }

        // EFECTOS
        if (proyecto.getArbolDeProblemas().getEfectos() != null) {

            proyecto.getArbolDeProblemas()
                    .getEfectos()
                    .forEach(efecto -> {

                        rubros.add(
                                RubroEvaluacionDTO.builder()
                                        .tipo("EFECTO")
                                        .referenciaId(efecto.getId())
                                        .descripcion(efecto.getDescripcion())
                                        .build()
                        );
                    });
        }
    }

    // =========================================================
    // ÁRBOL DE OBJETIVOS
    // =========================================================

    if (proyecto.getArbolDeObjetivos() != null) {

        // MEDIOS
        if (proyecto.getArbolDeObjetivos().getMedios() != null) {

            proyecto.getArbolDeObjetivos()
                    .getMedios()
                    .forEach(medio -> {

                        rubros.add(
                                RubroEvaluacionDTO.builder()
                                        .tipo("MEDIO")
                                        .referenciaId(medio.getId())
                                        .descripcion(medio.getDescripcion())
                                        .build()
                        );
                    });
        }

        // FINES
        if (proyecto.getArbolDeObjetivos().getFines() != null) {

            proyecto.getArbolDeObjetivos()
                    .getFines()
                    .forEach(fin -> {

                        rubros.add(
                                RubroEvaluacionDTO.builder()
                                        .tipo("FIN")
                                        .referenciaId(fin.getId())
                                        .descripcion(fin.getDescripcion())
                                        .build()
                        );
                    });
        }
    }

    return rubros;
}


@Transactional
public void eliminar(Long id) {

    Calificacion calificacion = calificacionRepository.findById(id)
            .orElseThrow(() ->
                    new IllegalArgumentException("Calificación no encontrada"));

    calificacionRepository.delete(calificacion);
}


@Transactional
public void eliminarEvaluacionesPorRol(
        Long calificacionId,
        RolCalificador rol) {

    Calificacion calificacion = calificacionRepository.findById(calificacionId)
            .orElseThrow(() ->
                    new IllegalArgumentException("Calificación no encontrada"));

    // =====================================================
    // VERIFICAR SI EXISTEN EVALUACIONES DEL OTRO ROL
    // =====================================================

    RolCalificador otroRol =
            rol == RolCalificador.TUTOR
                    ? RolCalificador.EVALUADOR
                    : RolCalificador.TUTOR;

    boolean existeOtroRol =
            calificacion.getRubros().stream()
                    .flatMap(r -> r.getEvaluaciones().stream())
                    .anyMatch(ev -> ev.getRolCalificador() == otroRol);

    // =====================================================
    // ELIMINAR EVALUACIONES DEL ROL
    // =====================================================

    for (RubroCalificacion rubro : calificacion.getRubros()) {

        rubro.getEvaluaciones().removeIf(
                ev -> ev.getRolCalificador() == rol
        );
    }

    // =====================================================
    // SI NO EXISTEN EVALUACIONES DEL OTRO ROL 
    // // ELIMINAR ALL
    // =====================================================

    if (!existeOtroRol) {

        calificacionRepository.delete(calificacion);

        return;
    }

    // =====================================================
    // ELIMINAR RUBROS VACÍOS
    // =====================================================

    calificacion.getRubros().removeIf(
            r -> r.getEvaluaciones().isEmpty()
    );

    // =====================================================
    // RECALCULAR NOTAS
    // =====================================================

    Float notaTutor =
            calcularPromedioPorRol(
                    calificacion,
                    RolCalificador.TUTOR
            );

    Float notaEvaluador =
            calcularPromedioPorRol(
                    calificacion,
                    RolCalificador.EVALUADOR
            );

    calificacion.setNotaTutor(notaTutor);

    calificacion.setNotaEvaluador(notaEvaluador);

    // =====================================================
    // RECALCULAR FINAL
    // =====================================================

    if (notaTutor != null &&
            notaEvaluador != null) {

        float notaFinal =
                (notaTutor + notaEvaluador) / 2;

        calificacion.setCalificacionFinal(notaFinal);

        asignarEstadoYSemaforo(
                calificacion,
                notaFinal);

    } else {

        calificacion.setCalificacionFinal(null);

        calificacion.setEstado(null);

        calificacion.setSemaforo(
                SemaforoEnum.Pendiente);
    }

    calificacionRepository.save(calificacion);
}

public List<CalificacionDTO> obtenerCalificacionesPorIdUsuarioInvestigador(Long usuarioInvestigadorId) {
        return calificacionRepository.findByUsuarioInvestigadorId(usuarioInvestigadorId).stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
}

public List<CalificacionDTO> obtenerCalificacionesPorIdUsuarioTutor(Long usuarioTutorId) {
        return calificacionRepository.findByUsuarioTutorId(usuarioTutorId).stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
}
 
public List<CalificacionDTO> obtenerCalificacionesPorIdUsuarioEvaluador(Long usuarioEvaluadorId) {
        return calificacionRepository.findByUsuarioEvaluadorId(usuarioEvaluadorId).stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
}

}
