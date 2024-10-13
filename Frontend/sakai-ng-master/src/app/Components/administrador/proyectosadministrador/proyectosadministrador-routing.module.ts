import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ProyectosAdministradorComponent } from './proyectosadministrador.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ProyectosAdministradorComponent }
    ])],
    exports: [RouterModule]
})
export class ProyectosAdministradorRoutingModule { }
