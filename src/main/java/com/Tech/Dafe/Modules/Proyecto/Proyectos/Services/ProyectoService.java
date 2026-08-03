package com.Tech.Dafe.Modules.Proyecto.Proyectos.Services;

import com.Tech.Dafe.Modules.Proyecto.Proyectos.DTO.ProyectoDTO;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.ss.usermodel.Workbook;

public interface ProyectoService {

    Proyecto crearProyecto(Proyecto proyecto);
    Proyecto actualizarProyecto(Long id, ProyectoDTO dto);
    public Iterable<Proyecto> findAll();
    public Optional<Proyecto> findById(Long id);
    public void eliminarProyecto(Long id);
    List<Proyecto> obtenerProyectosPorIdUsuario(Long usuarioId);
    public void validarProyecto(Proyecto proyecto);
    public void validarTituloProyecto(String titulo, Long id);

    // ==========================
    // EXPORTACIÓN
    // ==========================
    byte[] exportarProyectoExcel(Long id);
    byte[] exportarProyectoPdf(Long id);


    // ==========================
    // NUEVO: IMPORTACIÓN
    // ==========================
    Proyecto importarExcel(Workbook workbook);
    Proyecto importarPdf(PDDocument document);

}
