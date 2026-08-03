import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FormCalificacionTutComponent } from './formcalificaciontut.component';
import { FormCalificacionTutRoutingModule } from './formcalificaciontut-routing.module';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToggleButtonModule } from 'primeng/togglebutton';
import { RippleModule } from 'primeng/ripple';
import { MultiSelectModule } from 'primeng/multiselect';
import { DropdownModule } from 'primeng/dropdown';
import { ProgressBarModule } from 'primeng/progressbar';
import { ToastModule } from 'primeng/toast';
import { SliderModule } from 'primeng/slider';
import { RatingModule } from 'primeng/rating';
import { TagModule } from 'primeng/tag';
import { DialogModule } from 'primeng/dialog';
import { SplitButtonModule } from 'primeng/splitbutton';
import { TooltipModule } from 'primeng/tooltip';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
	imports: [
		CommonModule,
		FormCalificacionTutRoutingModule,
		FormsModule,
		TableModule,
		RatingModule,
		ButtonModule,
		SliderModule,
		InputTextModule,
		ToggleButtonModule,
		RippleModule,
		MultiSelectModule,
		DropdownModule,
		ProgressBarModule,
		ToastModule,
        TagModule,
        DialogModule,
        FormsModule,
        SplitButtonModule,
        ToastModule,
		TooltipModule,
		ReactiveFormsModule

	],
	declarations: [FormCalificacionTutComponent],
    providers: []
})
export class FormCalificacionTutModule { }