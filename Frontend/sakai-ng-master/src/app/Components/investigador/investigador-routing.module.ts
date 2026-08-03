import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '@app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'convocatoriasinvestigador', loadChildren: () => import('./convocatoriasinvestigador/convocatoriasinvestigador.module').then(m => m.ConvocatoriasInvestigadorModule), canActivate: [AuthGuard] },
        { path: 'proyectosinvestigador', loadChildren: () => import('./proyectosinvestigador/proyectosinvestigador.module').then(m => m.ProyectosInvestigadorModule), canActivate: [AuthGuard] },
        { path: 'inscripcionesinvestigador', loadChildren: () => import('./inscripcionesinvestigador/inscripcionesinvestigador.module').then(m => m.InscripcionesInvestigadorModule), canActivate: [AuthGuard] },
        { path: 'asignacionderolesinvestigador', loadChildren: () => import('./asignacionderolesinvestigador/asignacionderolesinvestigador.module').then(m => m.AsignacionDeRolesInvestigadorModule), canActivate: [AuthGuard] },
        { path: 'calificacionesinvestigador', loadChildren: () => import('./calificacionesinvestigador/calificacionesinvestigador.module').then(m => m.CalificacionesInvestigadorModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class InvestigadorRoutingModule { }
