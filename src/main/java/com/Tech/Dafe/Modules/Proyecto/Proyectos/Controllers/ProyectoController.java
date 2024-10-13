package com.Tech.Dafe.Modules.Proyecto.Proyectos.Controllers;

import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;

import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.DTO.ProyectoDTO;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Services.ProyectoService;
import com.Tech.Dafe.Modules.Usuario.Services.UsuarioService;
import com.Tech.Dafe.Response.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/proyecto")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok().body(proyectoService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar los elementos: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> ver(@PathVariable Long id) {
        try {
            Optional<Proyecto> o = proyectoService.findById(id);
            if (o.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Elemento no encontrado con ID: " + id);
            }
            return ResponseEntity.ok().body(o.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener el elemento: " + e.getMessage());
        }
    }


    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ProyectoDTO proyectoDTO) {
        try {
            if (proyectoDTO == null) {
                return ResponseEntity.badRequest().body(new Mensaje("La entidad proporcionada es nula."));
            }

            Usuario usuario = usuarioService.findById(proyectoDTO.getIdUsuario()).orElse(null);
            if (usuario == null) {
                return ResponseEntity.badRequest().body(new Mensaje("El usuario no existe."));
            }

            Proyecto proyecto = new Proyecto();
            proyecto.setDescripcion(proyectoDTO.getDescripcion());
            proyecto.setEstado(proyectoDTO.getEstado());

            String fechaActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            proyecto.setFecha_creacion(fechaActual);
            proyecto.setFecha_actualizacion(fechaActual);

            proyecto.setJustificacion(proyectoDTO.getJustificacion());
            proyecto.setObservaciones(proyectoDTO.getObservaciones());
            proyecto.setPoblacion_objetivo(proyectoDTO.getPoblacion_objetivo());
            proyecto.setPresupuesto(proyectoDTO.getPresupuesto());
            proyecto.setResultados_esperados(proyectoDTO.getResultados_esperados());
            proyecto.setTitulo(proyectoDTO.getTitulo());
            proyecto.setUsuario(usuario);

            Proyecto proyectoDb = proyectoService.save(proyecto);
            return ResponseEntity.status(HttpStatus.CREATED).body(proyectoDb);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Mensaje("Error al crear el proyecto: " + e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (!proyectoService.findById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Elemento no encontrado con ID: " + id);
            }
            proyectoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el elemento: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> ActualizarProyecto(@PathVariable Long id, @RequestBody Proyecto proyecto) {
        Optional<Proyecto> o = proyectoService.findById(id);

        Proyecto proyectoDb = o.get();
        proyectoDb.setDescripcion(proyecto.getDescripcion());
        proyectoDb.setEstado(proyecto.getEstado());
        proyectoDb.setJustificacion(proyecto.getJustificacion());
        proyectoDb.setObservaciones(proyecto.getObservaciones());
        proyectoDb.setPoblacion_objetivo(proyecto.getPoblacion_objetivo());
        proyectoDb.setPresupuesto(proyecto.getPresupuesto());
        proyectoDb.setResultados_esperados(proyecto.getResultados_esperados());
        proyectoDb.setTitulo(proyecto.getTitulo());

        String fechaActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        proyectoDb.setFecha_actualizacion(fechaActual);

        return ResponseEntity.ok(proyectoService.save(proyectoDb));
    }


    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Se ha producido un error inesperado: " + e.getMessage());
    }


}
