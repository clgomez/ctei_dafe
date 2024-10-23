import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AsignacionDeRolesAdminRoutingModule } from './asignacionderolesadmin-routing.module';
import { AsignacionDeRolesAdminComponent } from './asignacionderolesadmin.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
    imports: [
        CommonModule,
        AsignacionDeRolesAdminRoutingModule,
        FormsModule, TableModule, ButtonModule, TooltipModule
    ],
    declarations: [AsignacionDeRolesAdminComponent]
})
export class AsignacionDeRolesAdminModule { }
