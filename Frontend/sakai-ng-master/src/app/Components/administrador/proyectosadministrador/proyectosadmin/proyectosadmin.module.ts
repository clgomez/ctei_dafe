import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProyectosAdminRoutingModule } from './proyectosadmin-routing.module';
import { ProyectosAdminComponent } from './proyectosadmin.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
    imports: [
        CommonModule,
        ProyectosAdminRoutingModule,
        FormsModule, TableModule, ButtonModule, DialogModule, TooltipModule
    ],
    declarations: [ProyectosAdminComponent]
})
export class ProyectosAdminModule { }
