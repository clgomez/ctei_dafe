import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormActividadesInvestRoutingModule } from './formactividadesinvest-routing.module';
import { FormActividadesInvestComponent } from './formactividadesinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { HttpClientModule } from '@angular/common/http';
import { CalendarModule } from 'primeng/calendar';
import { TagModule } from 'primeng/tag';

@NgModule({
    imports: [
        CommonModule,
        FormActividadesInvestRoutingModule,
        FormsModule, HttpClientModule, TableModule, ButtonModule, CalendarModule, TagModule
    ],
    declarations: [FormActividadesInvestComponent]
})
export class FormActividadesInvestModule { }
