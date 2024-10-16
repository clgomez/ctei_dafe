import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AsignacionDeRolesAdminRoutingModule } from './asignacionderolesadmin-routing.module';
import { AsignacionDeRolesAdminComponent } from './asignacionderolesadmin.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        AsignacionDeRolesAdminRoutingModule,
        FormsModule
    ],
    declarations: [AsignacionDeRolesAdminComponent]
})
export class AsignacionDeRolesAdminModule { }
