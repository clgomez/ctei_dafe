import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConvocatoriasAdministradorRoutingModule } from './convocatoriasadministrador-routing.module';
import { ConvocatoriasAdministradorComponent } from './convocatoriasadministrador.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
    imports: [
        CommonModule,
        ConvocatoriasAdministradorRoutingModule,
        FormsModule, HttpClientModule, TableModule, ButtonModule
    ],
    declarations: [ConvocatoriasAdministradorComponent]
})
export class ConvocatoriasAdministradorModule { }
