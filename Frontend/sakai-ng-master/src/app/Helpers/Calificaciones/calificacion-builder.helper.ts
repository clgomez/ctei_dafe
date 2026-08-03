import { Calificacion } from '@app/Models/calificacion.model';
import { FieldCalificacion } from '@app/Interfaces/Calificaciones/fieldcalificacion.interface';
import { Usuario } from '@app/Models/usuario.model';
import { Proyecto } from '@app/Models/proyecto.model';
import { RolCalificador } from '@app/Enums/Calificaciones/rolcalificador.enum';
import { CalificacionMapperHelper } from './calificacion-mapper.helper';

export class CalificacionBuilderHelper {

  static construirCalificacion(
    esActualizacion: boolean,
    proyecto: Proyecto,
    usuario: Usuario,
    rol: RolCalificador,
    calificacionOriginal: Calificacion,
    fields: FieldCalificacion[]
  ): Calificacion {

    const calificacion = new Calificacion();

    // ==========================================
    // ID
    // ==========================================

    if (esActualizacion) {

      calificacion.id =
        calificacionOriginal.id;
    }

    // ==========================================
    // IDS PRINCIPALES
    // ==========================================

    calificacion.proyectoId =
      proyecto.id;

    // Tutor
    if (rol === RolCalificador.TUTOR) {

      calificacion.tutorId =
        usuario.id;
    }

    // Evaluador
    if (rol === RolCalificador.EVALUADOR) {

      calificacion.evaluadorId =
        usuario.id;
    }

    // Mantener rubros
    calificacion.rubros = [];

    // ==========================================
    // RUBROS
    // ==========================================

    for (const field of fields) {

      const rubroOriginal =
        esActualizacion
          ? CalificacionMapperHelper.buscarRubro(
              calificacionOriginal,
              field.tipoCalificacion,
              field.referenciaId
            )
          : undefined;

      const evaluacionOriginal =
        rubroOriginal
          ? CalificacionMapperHelper.buscarEvaluacionPorRolYUsuario(
              rubroOriginal,
              rol,
              usuario.id
            )
          : undefined;

      calificacion.rubros.push({

        // ======================================
        // RUBRO
        // ======================================

        id:
          esActualizacion
            ? rubroOriginal?.id
            : undefined,

        tipoCalificacion:
          field.tipoCalificacion,

        referenciaId:
          field.referenciaId ?? null,

        nombre:
          field.descripcion,

        // ======================================
        // EVALUACIONES
        // ======================================

        evaluaciones: [

          {
            id:
              evaluacionOriginal?.id,

            usuarioId:
              usuario.id,

            nombreUsuario:
              `${usuario.nombre} ${usuario.apellidos}`,

            rolCalificador:
              rol,

            nota:
              field.puntaje,

            comentario:
              field.comentarios,

            activo: true
          }
        ]
      });
    }

    return calificacion;
  }
}