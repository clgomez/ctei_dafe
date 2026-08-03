import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArbolDeProblemasInvestRoutingModule } from './formarboldeproblemasinvest-routing.module';
import { FormArbolDeProblemasInvestComponent } from './formarboldeproblemasinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ReactiveFormsModule } from '@angular/forms';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
    imports: [
        CommonModule,
        FormArbolDeProblemasInvestRoutingModule, ReactiveFormsModule,
        FormsModule, TableModule, ButtonModule, TagModule, TooltipModule
    ],
    declarations: [FormArbolDeProblemasInvestComponent]
})
export class FormArbolDeProblemasInvestModule { }

