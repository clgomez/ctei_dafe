package com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Service;

import com.Tech.Dafe.Modules.Enums.Roles.RolNombre;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.DTO.ConvocatoriaDTO;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Models.Convocatoria;
import com.Tech.Dafe.Modules.Convocatoria.Convocatorias.Repository.ConvocatoriaRepository;
import com.Tech.Dafe.Modules.Notificaciones.Services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConvocatoriaServiceImpl implements ConvocatoriaService {

    @Autowired
    private ConvocatoriaRepository convocatoriaRepository;

    @Autowired
    private NotificacionService notificacionService;


    public Convocatoria crearConvocatoria(ConvocatoriaDTO convocatoriaDTO) {
        Convocatoria convocatoria = new Convocatoria();
        convocatoria.setDescripcion(convocatoriaDTO.getDescripcion());
        convocatoria.setEstado("ACTIVA");
        convocatoria.setFechaFin(convocatoriaDTO.getFechaFin());
        convocatoria.setFechaInicio(convocatoriaDTO.getFechaInicio());

        Convocatoria nuevaConvocatoria = convocatoriaRepository.save(convocatoria);
        notificacionService.enviarNotificacionesPorRol(RolNombre.ROL_INVESTIGADOR, "Nueva convocatoria creada: " + nuevaConvocatoria.getDescripcion());

        return nuevaConvocatoria;
    }


    public List<Convocatoria> obtenerConvocatorias() {
        return convocatoriaRepository.findAll();
    }

    public Convocatoria obtenerConvocatoriaPorId(Long id) {
        return convocatoriaRepository.findById(id).orElse(null);
    }

    public Convocatoria actualizarConvocatoria(Long id, ConvocatoriaDTO convocatoriaDTO) {
        Convocatoria convocatoria = convocatoriaRepository.findById(id).orElse(null);
        if (convocatoria != null) {
            convocatoria.setDescripcion(convocatoriaDTO.getDescripcion());
            convocatoria.setEstado(convocatoriaDTO.getEstado());
            convocatoria.setFechaFin(convocatoriaDTO.getFechaFin());
            convocatoria.setFechaInicio(convocatoriaDTO.getFechaInicio());
            return convocatoriaRepository.save(convocatoria);
        }
        return null;
    }

    public void eliminarConvocatoria(Long id) {
        convocatoriaRepository.deleteById(id);
    }

}
