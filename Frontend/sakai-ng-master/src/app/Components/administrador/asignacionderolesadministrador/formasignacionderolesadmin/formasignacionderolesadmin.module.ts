import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormAsignacionDeRolesAdminRoutingModule } from './formasignacionderolesadmin-routing.module';
import { FormAsignacionDeRolesAdminComponent } from './formasignacionderolesadmin.component';
import { FormsModule } from '@angular/forms';
import { TagModule } from 'primeng/tag';

@NgModule({
    imports: [
        CommonModule,
        FormAsignacionDeRolesAdminRoutingModule,
        FormsModule, TagModule
    ],
    declarations: [FormAsignacionDeRolesAdminComponent]
})
export class FormAsignacionDeRolesAdminModule { }

