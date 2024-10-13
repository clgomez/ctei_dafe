import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AsignacionDeRolesAdministradorRoutingModule } from './asignacionderolesadministrador-routing.module';
import { AsignacionDeRolesAdministradorComponent } from './asignacionderolesadministrador.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        AsignacionDeRolesAdministradorRoutingModule,
        FormsModule
    ],
    declarations: [AsignacionDeRolesAdministradorComponent]
})
export class AsignacionDeRolesAdministradorModule { }
