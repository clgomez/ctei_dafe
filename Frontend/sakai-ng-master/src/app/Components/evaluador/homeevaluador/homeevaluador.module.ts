import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeEvaluadorRoutingModule } from './homeevaluador-routing.module';
import { HomeEvaluadorComponent } from './homeevaluador.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        HomeEvaluadorRoutingModule,
        FormsModule
    ],
    declarations: [HomeEvaluadorComponent]
})
export class HomeEvaluadorModule { }
