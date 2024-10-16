package com.Tech.Dafe.Modules.Calificaciones.Service;

import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Models.ArbolDeObjetivos;
import com.Tech.Dafe.Modules.Arboles.Objetivos.ArbolObjetivos.Repository.ArbolDeObjetivosRepository;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Models.Fines;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Fines.Repository.FinesRepository;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Models.Medio;
import com.Tech.Dafe.Modules.Arboles.Objetivos.Medio.Repository.MedioRepository;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Models.ArbolDeProblemas;
import com.Tech.Dafe.Modules.Arboles.Problemas.ArbolProblemas.Repository.ArbolProblemaRepository;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Models.Causa;
import com.Tech.Dafe.Modules.Arboles.Problemas.Causas.Repository.CausaRepository;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Models.Efecto;
import com.Tech.Dafe.Modules.Arboles.Problemas.Efectos.Repository.EfectoRepository;
import com.Tech.Dafe.Modules.Calificaciones.Repository.CalificacionesRepository;
import com.Tech.Dafe.Modules.Calificaciones.DTO.CalificacionesDTO;
import com.Tech.Dafe.Modules.Calificaciones.Models.Calificacion;
import com.Tech.Dafe.Modules.Actividades.Cronograma.Repository.CronogramaRepository;
import com.Tech.Dafe.Modules.Notificaciones.Services.NotificacionServiceImpl;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Repositorio.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalificacionesService {

    @Autowired
    private ProyectoRepository proyectoRepository;
    @Autowired
    private CalificacionesRepository calificacionRepository;
    @Autowired
    private EfectoRepository efectoRepository;
    @Autowired
    private FinesRepository finesRepository;
    @Autowired
    private ArbolProblemaRepository arbolDeProblemasRepository;
    @Autowired
    private ArbolDeObjetivosRepository arbolDeObjetivosRepository;
    @Autowired
    private CausaRepository causaRepository;
    @Autowired
    private MedioRepository medioRepository;
    @Autowired
    private CronogramaRepository cronogramaRepository;
    @Autowired
    private NotificacionServiceImpl notificacionService;

    public Calificacion crearCalificacion(CalificacionesDTO calificacionDTO) {
        Proyecto proyecto = proyectoRepository.findById(calificacionDTO.getProyectoId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con ID: " + calificacionDTO.getProyectoId()));

        Calificacion calificacion = new Calificacion();
        calificacion.setCalificacionDescripcion(calificacionDTO.getCalificacionDescripcion());
        calificacion.setCalificacionJustificacion(calificacionDTO.getCalificacionJustificacion());
        calificacion.setCalificacionPoblacionObjetivo(calificacionDTO.getCalificacionPoblacionObjetivo());
        calificacion.setCalificacionPresupuesto(calificacionDTO.getCalificacionPresupuesto());
        calificacion.setCalificacionTitulo(calificacionDTO.getCalificacionTitulo());
        calificacion.setComentario(calificacionDTO.getComentario());
        calificacion.setEstado(calificacionDTO.getEstado());
        calificacion.setResultadoEsperado(calificacionDTO.getResultadoEsperado());
        calificacion.setSemaforo(calificacionDTO.getSemaforo());
        calificacion.setProyecto(proyecto);

        float calificacionCausa = 0;
        float calificacionEfecto = 0;
        float calificacionMedio = 0;
        float calificacionFin = 0;

        if (proyecto.getArbolDeProblemas() != null) {
            List<Causa> causas = proyecto.getArbolDeProblemas().getCausas();
            for (Causa causa : causas) {
                calificacionCausa += causa.getCalificacion();
            }
        }
        if (proyecto.getArbolDeProblemas() != null) {
            List<Efecto> efectos = proyecto.getArbolDeProblemas().getEfectos();
            for (Efecto efecto : efectos) {
                calificacionEfecto += efecto.getCalificacion();
            }
        }
        if (proyecto.getArbolDeObjetivos() != null) {
            List<Medio> medios = proyecto.getArbolDeObjetivos().getMedios();
            for (Medio medio : medios) {
                calificacionMedio += medio.getCalificacion();
            }
        }
        if (proyecto.getArbolDeObjetivos() != null) {
            List<Fines> fines = proyecto.getArbolDeObjetivos().getFines();
            for (Fines fin : fines) {
                calificacionFin += fin.getCalificacion();
            }
        }

        float sumaCalificaciones = calificacionDTO.getCalificacionDescripcion() +
                calificacionDTO.getCalificacionJustificacion() +
                calificacionDTO.getCalificacionPoblacionObjetivo() +
                calificacionDTO.getCalificacionPresupuesto() +
                calificacionDTO.getCalificacionTitulo() +
                calificacionCausa +
                calificacionEfecto +
                calificacionMedio +
                calificacionFin;

        int cantidadCalificaciones = 0;
        cantidadCalificaciones++;
        cantidadCalificaciones += (calificacionDTO.getCalificacionJustificacion() >= 0) ? 1 : 0;
        cantidadCalificaciones += (calificacionDTO.getCalificacionPoblacionObjetivo() >= 0) ? 1 : 0;
        cantidadCalificaciones += (calificacionDTO.getCalificacionPresupuesto() >= 0) ? 1 : 0;
        cantidadCalificaciones += (calificacionDTO.getCalificacionTitulo() >= 0) ? 1 : 0;
        cantidadCalificaciones += (calificacionCausa >= 0) ? 1 : 0;
        cantidadCalificaciones += (calificacionEfecto >= 0) ? 1 : 0;
        cantidadCalificaciones += (calificacionMedio >= 0) ? 1 : 0;
        cantidadCalificaciones += (calificacionFin >= 0) ? 1 : 0;

        float calificacionFinal = (cantidadCalificaciones > 0) ? (sumaCalificaciones / cantidadCalificaciones) : 0f;
        calificacion.setCalificacionFinal(calificacionFinal);

        if (proyecto.getUsuario() != null) {
            Long usuarioId = proyecto.getUsuario().getId();
            calificacion.setIdUsuario(usuarioId);

            Long inscripcionId = 0L;
            if (proyecto.getInscripciones() != null && !proyecto.getInscripciones().isEmpty()) {
                inscripcionId = proyecto.getInscripciones().get(0).getId();
            }
            calificacion.setIdInscripcion(inscripcionId);

            Calificacion savedCalificacion = calificacionRepository.save(calificacion);

            String mensaje = "Se ha calificado el proyecto '" + proyecto.getTitulo() + "' con la calificación: " + savedCalificacion.getCalificacionFinal();

            notificacionService.enviarNotificacionPorCalificacionYInscripcion(usuarioId, mensaje, savedCalificacion.getId(), inscripcionId);
        }

        ArbolDeObjetivos arbolDeObjetivos = proyecto.getArbolDeObjetivos();
        if (arbolDeObjetivos != null) {
            calificacion.setArbolDeObjetivos(arbolDeObjetivos);
            List<Medio> medios = arbolDeObjetivos.getMedios();
            if (medios != null && !medios.isEmpty()) {
                calificacion.setMedio(medios.get(0));
            }
            List<Fines> fines = arbolDeObjetivos.getFines();
            if (fines != null && !fines.isEmpty()) {
                calificacion.setFin(fines.get(0));
            }
        }

        ArbolDeProblemas arbolDeProblemas = proyecto.getArbolDeProblemas();
        if (arbolDeProblemas != null) {
            calificacion.setArbolDeProblemas(arbolDeProblemas);
            List<Causa> causas = arbolDeProblemas.getCausas();
            if (causas != null && !causas.isEmpty()) {
                calificacion.setCausa(causas.get(0));
            }
            List<Efecto> efectos = arbolDeProblemas.getEfectos();
            if (efectos != null && !efectos.isEmpty()) {
                calificacion.setEfecto(efectos.get(0));
            }
        }

        calificacion.setCronograma(cronogramaRepository.findById(calificacionDTO.getCronogramaId()).orElse(null));
        return calificacionRepository.save(calificacion);
    }

    public Calificacion actualizarCalificacion(Long calificacionId, CalificacionesDTO calificacionDTO) {
        Calificacion calificacionExistente = calificacionRepository.findById(calificacionId)
                .orElseThrow(() -> new IllegalArgumentException("Calificación no encontrada con ID: " + calificacionId));

        calificacionExistente.setCalificacionDescripcion(calificacionDTO.getCalificacionDescripcion());
        calificacionExistente.setCalificacionJustificacion(calificacionDTO.getCalificacionJustificacion());
        calificacionExistente.setCalificacionPoblacionObjetivo(calificacionDTO.getCalificacionPoblacionObjetivo());
        calificacionExistente.setCalificacionPresupuesto(calificacionDTO.getCalificacionPresupuesto());
        calificacionExistente.setCalificacionTitulo(calificacionDTO.getCalificacionTitulo());
        calificacionExistente.setComentario(calificacionDTO.getComentario());
        calificacionExistente.setEstado(calificacionDTO.getEstado());
        calificacionExistente.setResultadoEsperado(calificacionDTO.getResultadoEsperado());
        calificacionExistente.setSemaforo(calificacionDTO.getSemaforo());

        recalcularCalificacionFinal(calificacionExistente, calificacionDTO);

        if (calificacionExistente.getProyecto() != null && calificacionExistente.getProyecto().getUsuario() != null) {
            Long usuarioId = calificacionExistente.getProyecto().getUsuario().getId();

            Long inscripcionId = 0L;
            if (calificacionExistente.getProyecto().getInscripciones() != null
                    && !calificacionExistente.getProyecto().getInscripciones().isEmpty()) {
                inscripcionId = calificacionExistente.getProyecto().getInscripciones().get(0).getId();
            }

            String mensaje = "Se ha actualizado la calificación del proyecto '" + calificacionExistente.getProyecto().getTitulo() +
                    "' con la nueva calificación: " + calificacionExistente.getCalificacionFinal();

            notificacionService.enviarNotificacionPorCalificacionYInscripcion(usuarioId, mensaje, calificacionExistente.getId(), inscripcionId);
        }

        return calificacionRepository.save(calificacionExistente);
    }


    private void recalcularCalificacionFinal(Calificacion calificacion, CalificacionesDTO calificacionDTO) {
        float calificacionCausa = 0;
        float calificacionEfecto = 0;
        float calificacionMedio = 0;
        float calificacionFin = 0;

        if (calificacion.getProyecto().getArbolDeProblemas() != null) {
            List<Causa> causas = calificacion.getProyecto().getArbolDeProblemas().getCausas();
            for (Causa causa : causas) {
                calificacionCausa += causa.getCalificacion();
            }
        }
        if (calificacion.getProyecto().getArbolDeProblemas() != null) {
            List<Efecto> efectos = calificacion.getProyecto().getArbolDeProblemas().getEfectos();
            for (Efecto efecto : efectos) {
                calificacionEfecto += efecto.getCalificacion();
            }
        }
        if (calificacion.getProyecto().getArbolDeObjetivos() != null) {
            List<Medio> medios = calificacion.getProyecto().getArbolDeObjetivos().getMedios();
            for (Medio medio : medios) {
                calificacionMedio += medio.getCalificacion();
            }
        }
        if (calificacion.getProyecto().getArbolDeObjetivos() != null) {
            List<Fines> fines = calificacion.getProyecto().getArbolDeObjetivos().getFines();
            for (Fines fin : fines) {
                calificacionFin += fin.getCalificacion();
            }
        }

        float sumaCalificaciones = calificacionDTO.getCalificacionDescripcion() +
                calificacionDTO.getCalificacionJustificacion() +
                calificacionDTO.getCalificacionPoblacionObjetivo() +
                calificacionDTO.getCalificacionPresupuesto() +
                calificacionDTO.getCalificacionTitulo() +
                calificacionCausa +
                calificacionEfecto +
                calificacionMedio +
                calificacionFin;

        int cantidadCalificaciones = 0;
        cantidadCalificaciones++;
        cantidadCalificaciones += (calificacionDTO.getCalificacionJustificacion() >= 0) ? 1 : 0;
        cantidadCalificaciones += (calificacionDTO.getCalificacionPoblacionObjetivo() >= 0) ? 1 : 0;
        cantidadCalificaciones += (calificacionDTO.getCalificacionPresupuesto() >= 0) ? 1 : 0;
        cantidadCalificaciones += (calificacionDTO.getCalificacionTitulo() >= 0) ? 1 : 0;
        cantidadCalificaciones += (calificacionCausa >= 0) ? 1 : 0;
        cantidadCalificaciones += (calificacionEfecto >= 0) ? 1 : 0;
        cantidadCalificaciones += (calificacionMedio >= 0) ? 1 : 0;
        cantidadCalificaciones += (calificacionFin >= 0) ? 1 : 0;

        float calificacionFinal = (cantidadCalificaciones > 0) ? (sumaCalificaciones / cantidadCalificaciones) : 0f;
        calificacion.setCalificacionFinal(calificacionFinal);
    }


    public List<Calificacion> obtenerTodasCalificaciones() {
        return calificacionRepository.findAll();
    }

    public List<Calificacion> obtenerCalificacionesPorProyecto(Long proyectoId) {
        return calificacionRepository.findByProyectoId(proyectoId);
    }

    public Calificacion obtenerCalificacionPorId(Long calificacionId) {
        return calificacionRepository.findById(calificacionId)
                .orElseThrow(() -> new IllegalArgumentException("Calificación no encontrada con ID: " + calificacionId));
    }

    public void eliminarCalificacion(Long calificacionId) {
        if (!calificacionRepository.existsById(calificacionId)) {
            throw new IllegalArgumentException("Calificación no encontrada con ID: " + calificacionId);
        }
        calificacionRepository.deleteById(calificacionId);
    }

}
