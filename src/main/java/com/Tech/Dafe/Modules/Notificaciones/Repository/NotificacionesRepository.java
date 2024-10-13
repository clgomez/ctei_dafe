package com.Tech.Dafe.Modules.Notificaciones.Repository;

import com.Tech.Dafe.Modules.Notificaciones.Models.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionesRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioId(Long usuarioId);
}
