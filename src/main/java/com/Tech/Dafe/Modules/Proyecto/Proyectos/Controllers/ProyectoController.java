package com.Tech.Dafe.Modules.Proyecto.Proyectos.Controllers;

import com.Tech.Dafe.Modules.Proyecto.Proyectos.Models.Proyecto;
import com.Tech.Dafe.Exceptions.BusinessException;
import com.Tech.Dafe.Exceptions.ResourceNotFoundException;
import com.Tech.Dafe.Modules.Autenticacion.Modelos.Usuario;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.DTO.ProyectoDTO;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Export.ExcelValidationService;
import com.Tech.Dafe.Modules.Proyecto.Proyectos.Services.ProyectoService;
import com.Tech.Dafe.Modules.Usuario.Repositorio.UsuarioRepository;
import com.Tech.Dafe.Modules.Usuario.Services.UsuarioService;
import com.Tech.Dafe.Response.CampoError;
import com.Tech.Dafe.Utils.NormalizarTitulo.ValidarTituloDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/proyecto")
@RequiredArgsConstructor
public class ProyectoController {
    
    private final ProyectoService proyectoService;

    private final UsuarioService usuarioService;

    private final UsuarioRepository usuarioRepository;

    private final ExcelValidationService excelValidationService;


    @GetMapping
    public ResponseEntity<?> listarProyectos() {
        return ResponseEntity.ok(proyectoService.findAll());
    }

   @GetMapping("/{id}")
    public ResponseEntity<?> ver(@PathVariable Long id) {

        Proyecto proyecto = proyectoService.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No existe el proyecto con ID " + id));

        return ResponseEntity.ok(proyecto);
    }

    @PostMapping
    public ResponseEntity<?> crearProyecto(@Valid @RequestBody ProyectoDTO proyectoDTO) {

        Usuario usuario = usuarioService.findById(proyectoDTO.getIdUsuario())
                .orElseThrow(() ->
                        new ResourceNotFoundException("No existe el usuario con ID " + proyectoDTO.getIdUsuario()));

        Proyecto proyecto = new Proyecto();

        proyecto.setTitulo(proyectoDTO.getTitulo());
        proyecto.setDescripcion(proyectoDTO.getDescripcion());
        proyecto.setPoblacionObjetivo(proyectoDTO.getPoblacionObjetivo());
        proyecto.setJustificacion(proyectoDTO.getJustificacion());
        proyecto.setPresupuesto(proyectoDTO.getPresupuesto());
        proyecto.setResultadosEsperados(proyectoDTO.getResultadosEsperados());
        proyecto.setObservaciones(proyectoDTO.getObservaciones());
        proyecto.setEstado(proyectoDTO.getEstado());
        proyecto.setFechaCreacion(LocalDateTime.now());
        proyecto.setUsuario(usuario);

        Proyecto proyectoDb = proyectoService.crearProyecto(proyecto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Proyecto creado exitosamente",
                        "proyecto", proyectoDb
                ));
    }


   @PutMapping("/{id}")
   public ResponseEntity<?> actualizarProyecto(
        @PathVariable Long id,
        @Valid @RequestBody ProyectoDTO proyectoDTO) {

        Proyecto actualizado =
        proyectoService.actualizarProyecto(id, proyectoDTO);

        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", "Proyecto actualizado exitosamente",
                "proyecto", actualizado
              )
        );
   }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProyecto(@PathVariable Long id) {

        Proyecto proyecto = proyectoService.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No existe el proyecto con ID " + id));

        proyectoService.eliminarProyecto(proyecto.getId());

        return ResponseEntity.ok(
                Map.of(
                        "codigo", "SUCCESS",
                        "mensaje", "Proyecto eliminado exitosamente"
                )
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerProyectosPorIdUsuario(@PathVariable Long usuarioId) {

        usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No existe el usuario con ID " + usuarioId));

        return ResponseEntity.ok(
                proyectoService.obtenerProyectosPorIdUsuario(usuarioId)
        );
    }   

    @PostMapping("/validar-titulo-proyecto")
    public ResponseEntity<?> validarTitulo(
                @Valid @RequestBody ValidarTituloDTO dto) {

        proyectoService.validarTituloProyecto(
                dto.getTitulo(),
                dto.getId()
        );

        return ResponseEntity.ok(
        Map.of(
                "codigo", "SUCCESS",
                "mensaje", "No existe ningún proyecto con ese título"
              )
        );
    }

    @GetMapping("/export/excel/{id}")
        public ResponseEntity<byte[]> exportarExcel(@PathVariable Long id) {

        byte[] archivo = proyectoService.exportarProyectoExcel(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=proyecto_" + id + ".xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(archivo);
   }

   @GetMapping("/export/pdf/{id}")
        public ResponseEntity<byte[]> exportarPdf(@PathVariable Long id) {

        byte[] archivo = proyectoService.exportarProyectoPdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=proyecto_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(archivo);
        }

        
    @PostMapping(
                 value = "/import/excel",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE
                )
    public ResponseEntity<?> importarExcel(@RequestParam("file") MultipartFile file,
                                           @RequestParam("usuarioId") Long usuarioId) throws Exception {

        if (file.isEmpty()) {
                throw new BusinessException(
                        List.of(
                                new CampoError(
                                        "archivo",
                                        "Debe seleccionar un archivo."
                                )
                        )
                );
        }

        String nombre = file.getOriginalFilename();

        if (nombre == null ||
                !(nombre.toLowerCase().endsWith(".xlsx")
                || nombre.toLowerCase().endsWith(".xls"))) {

                throw new BusinessException(
                        List.of(
                                new CampoError(
                                        "archivo",
                                        "Solo se permiten archivos Excel (.xlsx o .xls)."
                                )
                        )
                );
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

                excelValidationService.validate(workbook);

                Proyecto proyecto = proyectoService.importarExcel(workbook);

                Usuario usuario = usuarioService.findById(usuarioId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No existe el usuario con id " + usuarioId));

                proyecto.setUsuario(usuario);

                proyectoService.validarProyecto(proyecto);

                Proyecto creado = proyectoService.crearProyecto(proyecto);

                return ResponseEntity.ok(
                        Map.of(
                                "codigo", "SUCCESS",
                                "mensaje", "Proyecto importado correctamente.",
                                "proyecto", creado
                        )
                );

        } catch (BusinessException ex) {
                throw ex;

        } catch (org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException ex) {

                throw new BusinessException(
                        List.of(
                                new CampoError(
                                        "archivo",
                                        "El archivo Excel tiene un formato inválido o su contenido no corresponde a un archivo .xlsx."
                                )
                        )
                );

        } catch (org.apache.poi.EncryptedDocumentException ex) {

                throw new BusinessException(
                        List.of(
                                new CampoError(
                                        "archivo",
                                        "El archivo Excel está protegido con contraseña."
                                )
                        )
                );

        } catch (IOException ex) {

                throw new BusinessException(
                        List.of(
                                new CampoError(
                                        "archivo",
                                        "No fue posible leer el archivo Excel."
                                )
                        )
                );

        } /*catch (Exception ex) {

                throw new BusinessException(
                        List.of(
                                new CampoError(
                                        "archivo",
                                        "El archivo Excel no fue generado por DAFE o su estructura es inválida."
                                )
                        )
                );
        }*/
    }
    

    @PostMapping(
        value = "/import/pdf",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
        )
        public ResponseEntity<?> importarPdf(
                @RequestParam("file") MultipartFile file,
                @RequestParam("usuarioId") Long usuarioId) throws Exception {

        if (file.isEmpty()) {
                throw new BusinessException(
                        List.of(
                                new CampoError(
                                        "archivo",
                                        "Debe seleccionar un archivo."
                                )
                        )
                );
        }

        String nombre = file.getOriginalFilename();

        if (nombre == null || !nombre.toLowerCase().endsWith(".pdf")) {

                throw new BusinessException(
                        List.of(
                                new CampoError(
                                        "archivo",
                                        "Solo se permiten archivos PDF (.pdf)."
                                )
                        )
                );
        }

        try (PDDocument document = PDDocument.load(file.getInputStream())) {

                Proyecto proyecto = proyectoService.importarPdf(document);

                Usuario usuario = usuarioService.findById(usuarioId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No existe el usuario con id " + usuarioId));

                proyecto.setUsuario(usuario);

                proyectoService.validarProyecto(proyecto);

                Proyecto creado = proyectoService.crearProyecto(proyecto);

                return ResponseEntity.ok(
                        Map.of(
                                "codigo", "SUCCESS",
                                "mensaje", "Proyecto importado correctamente.",
                                "proyecto", creado
                        )
                );

        } catch (BusinessException ex) {

                throw ex;

        } catch (org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException ex) {

                throw new BusinessException(
                        List.of(
                                new CampoError(
                                        "archivo",
                                        "El archivo PDF está protegido con contraseña."
                                )
                        )
                );

        } catch (IOException ex) {

                throw new BusinessException(
                        List.of(
                                new CampoError(
                                        "archivo",
                                        "No fue posible leer el archivo PDF."
                                )
                        )
                );

        } /*catch (Exception ex) {

                throw new BusinessException(
                        List.of(
                                new CampoError(
                                        "archivo",
                                        "El archivo PDF no fue generado por DAFE o su estructura es inválida."
                                )
                        )
                );
        }*/
        }

        @PostMapping(
                value = "/test-upload",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE
        )
        public String upload(
                @RequestParam("file") MultipartFile file,
                @RequestParam("usuarioId") Long usuarioId) {

                return file.getOriginalFilename() + " - " + usuarioId;
        }

        /*
        @PostMapping(
                value = "/import/excel",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE
                )
                public ResponseEntity<?> importarExcel(
                        @RequestPart("file") MultipartFile file,
                        @RequestPart("usuarioId") String usuarioId)
                {
                System.out.println(file.getOriginalFilename());
                System.out.println(usuarioId);

                return ResponseEntity.ok("OK");
        }
        */

        /*
        @PostMapping(
                value = "/import/pdf",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE
                )
                public ResponseEntity<?> importarPdf(
                        @RequestPart("file") MultipartFile file,
                        @RequestPart("usuarioId") String usuarioId)
                {
                System.out.println(file.getOriginalFilename());
                System.out.println(usuarioId);

                return ResponseEntity.ok("OK");
        }
        */    
       
}
