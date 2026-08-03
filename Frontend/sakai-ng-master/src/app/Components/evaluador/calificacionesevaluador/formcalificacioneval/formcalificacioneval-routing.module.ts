import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormCalificacionEvalComponent } from './formcalificacioneval.component';

@NgModule({
	imports: [RouterModule.forChild([
		{ path: '', component: FormCalificacionEvalComponent }

	])],
	exports: [RouterModule]
})
export class FormCalificacionEvalRoutingModule { }