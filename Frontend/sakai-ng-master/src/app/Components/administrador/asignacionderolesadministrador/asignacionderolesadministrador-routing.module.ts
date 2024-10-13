import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AsignacionDeRolesAdministradorComponent } from './asignacionderolesadministrador.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: AsignacionDeRolesAdministradorComponent }
    ])],
    exports: [RouterModule]
})
export class AsignacionDeRolesAdministradorRoutingModule { }
