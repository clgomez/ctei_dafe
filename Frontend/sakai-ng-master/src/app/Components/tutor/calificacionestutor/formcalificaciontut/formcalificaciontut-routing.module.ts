import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormCalificacionTutComponent } from './formcalificaciontut.component';

@NgModule({
	imports: [RouterModule.forChild([
		{ path: '', component: FormCalificacionTutComponent }

	])],
	exports: [RouterModule]
})
export class FormCalificacionTutRoutingModule { }