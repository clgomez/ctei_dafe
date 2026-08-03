import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormProyectosInvestRoutingModule } from './formproyectosinvest-routing.module';
import { FormProyectosInvestComponent } from './formproyectosinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';

@NgModule({
    imports: [
        CommonModule,
        FormProyectosInvestRoutingModule,
        FormsModule, TableModule, ButtonModule, TagModule
    ],
    declarations: [FormProyectosInvestComponent]
})
export class FormProyectosInvestModule { }
