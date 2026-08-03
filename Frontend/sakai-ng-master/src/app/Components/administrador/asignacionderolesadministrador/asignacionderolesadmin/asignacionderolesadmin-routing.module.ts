import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AsignacionDeRolesAdminComponent } from './asignacionderolesadmin.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: AsignacionDeRolesAdminComponent }
    ])],
    exports: [RouterModule]
})
export class AsignacionDeRolesAdminRoutingModule { }
