import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectosAdminRoutingModule } from './proyectosadmin-routing.module';
import { ProyectosAdminComponent } from './proyectosadmin.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        ProyectosAdminRoutingModule,
        FormsModule
    ],
    declarations: [ProyectosAdminComponent]
})
export class ProyectosAdminModule { }
