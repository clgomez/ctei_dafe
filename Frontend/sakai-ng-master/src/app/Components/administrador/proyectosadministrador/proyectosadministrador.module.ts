import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectosAdministradorRoutingModule } from './proyectosadministrador-routing.module';
import { ProyectosAdministradorComponent } from './proyectosadministrador.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        ProyectosAdministradorRoutingModule,
        FormsModule
    ],
    declarations: [ProyectosAdministradorComponent]
})
export class ProyectosAdministradorModule { }
