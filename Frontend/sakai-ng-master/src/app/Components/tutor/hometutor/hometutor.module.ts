import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeTutorRoutingModule } from './hometutor-routing.module';
import { HomeTutorComponent } from './hometutor.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        HomeTutorRoutingModule,
        FormsModule
    ],
    declarations: [HomeTutorComponent]
})
export class HomeTutorModule { }
