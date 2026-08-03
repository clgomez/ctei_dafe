import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormVistaPreviaProyectosInvestRoutingModule } from './formvistapreviaproyectosinvest-routing.module';
import { FormVistaPreviaProyectosInvestComponent } from './formvistapreviaproyectosinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';

@NgModule({
    imports: [
        CommonModule,
        FormVistaPreviaProyectosInvestRoutingModule,
        FormsModule, TableModule, ButtonModule, TagModule
    ],
    declarations: [FormVistaPreviaProyectosInvestComponent]
})
export class FormVistaPreviaProyectosInvestModule { }
