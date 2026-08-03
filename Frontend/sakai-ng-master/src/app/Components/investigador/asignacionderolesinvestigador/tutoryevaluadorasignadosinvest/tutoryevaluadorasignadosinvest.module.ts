import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DatePipe } from '@angular/common';
import { TutorYEvaluadorAsignadosInvestRoutingModule } from './tutoryevaluadorasignadosinvest-routing.module';
import { TutorYEvaluadorAsignadosInvestComponent } from './tutoryevaluadorasignadosinvest.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';

@NgModule({
    imports: [
        CommonModule,
        TutorYEvaluadorAsignadosInvestRoutingModule,
        FormsModule, TableModule, ButtonModule, TooltipModule
    ],
    declarations: [TutorYEvaluadorAsignadosInvestComponent],
    providers: [DatePipe]
})
export class TutorYEvaluadorAsignadosInvestModule { }
