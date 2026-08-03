import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArbolDeObjetivosInvestRoutingModule } from './formarboldeobjetivosinvest-routing.module';
import { FormArbolDeObjetivosInvestComponent } from './formarboldeobjetivosinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        FormArbolDeObjetivosInvestRoutingModule, ReactiveFormsModule,
        FormsModule, TableModule, ButtonModule, TagModule
    ],
    declarations: [FormArbolDeObjetivosInvestComponent]
})
export class FormArbolDeObjetivosInvestModule { }

