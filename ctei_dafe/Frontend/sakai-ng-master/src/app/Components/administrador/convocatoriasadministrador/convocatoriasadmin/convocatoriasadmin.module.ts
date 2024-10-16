import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConvocatoriasAdminRoutingModule } from './convocatoriasadmin-routing.module';
import { ConvocatoriasAdminComponent } from './convocatoriasadmin.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
    imports: [
        CommonModule,
        ConvocatoriasAdminRoutingModule,
        FormsModule, HttpClientModule, TableModule, ButtonModule
    ],
    declarations: [ConvocatoriasAdminComponent]
})
export class ConvocatoriasAdminModule { }
