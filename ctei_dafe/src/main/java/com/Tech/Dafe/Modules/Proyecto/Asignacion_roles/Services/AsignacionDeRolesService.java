package com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.Services;

import com.Tech.Dafe.Modules.Proyecto.Asignacion_roles.DTO.AsignacionDeRolesDTO;
import java.util.List;
import java.util.Optional;

public interface AsignacionDeRolesService {

    AsignacionDeRolesDTO crearAsignacion(AsignacionDeRolesDTO asignacion);
    Optional<AsignacionDeRolesDTO> obtenerAsignacion(Long id);
    AsignacionDeRolesDTO actualizarAsignacion(Long id, AsignacionDeRolesDTO asignacionActualizada);
    void eliminarAsignacion(Long id);
    List<AsignacionDeRolesDTO> listarAsignaciones();

}
