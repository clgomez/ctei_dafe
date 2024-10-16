import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormConvocatoriasAdminRoutingModule } from './formconvocatoriasadmin-routing.module';
import { FormConvocatoriasAdminComponent } from './formconvocatoriasadmin.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { HttpClientModule } from '@angular/common/http';
import { CalendarModule } from 'primeng/calendar';
import { TagModule } from 'primeng/tag';

@NgModule({
    imports: [
        CommonModule,
        FormConvocatoriasAdminRoutingModule,
        FormsModule, HttpClientModule, TableModule, ButtonModule, CalendarModule, TagModule
    ],
    declarations: [FormConvocatoriasAdminComponent]
})
export class FormConvocatoriasAdminModule { }
