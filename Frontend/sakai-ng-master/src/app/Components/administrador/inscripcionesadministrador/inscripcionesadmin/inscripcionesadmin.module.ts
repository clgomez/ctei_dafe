import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InscripcionesAdminRoutingModule } from './inscripcionesadmin-routing.module';
import { InscripcionesAdminComponent } from './inscripcionesadmin.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
    imports: [
        CommonModule,
        InscripcionesAdminRoutingModule,
        FormsModule, TableModule, ButtonModule, TooltipModule
    ],
    declarations: [InscripcionesAdminComponent]
})
export class InscripcionesAdminModule { }
