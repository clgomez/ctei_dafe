import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'homeinvestigador', loadChildren: () => import('./homeinvestigador/homeinvestigador.module').then(m => m.HomeInvestigadorModule), canActivate: [AuthGuard] },
        { path: 'convocatoriasinvestigador', loadChildren: () => import('./convocatoriasinvestigador/convocatoriasinvestigador.module').then(m => m.ConvocatoriasInvestigadorModule), canActivate: [AuthGuard] },
        { path: 'proyectosinvestigador', loadChildren: () => import('./proyectosinvestigador/proyectosinvestigador.module').then(m => m.ProyectosInvestigadorModule), canActivate: [AuthGuard] },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class InvestigadorRoutingModule { }
