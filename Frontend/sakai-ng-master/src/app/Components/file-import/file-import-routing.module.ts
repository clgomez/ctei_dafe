import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FileImportComponent } from './file-import.component';

@NgModule({
	imports: [RouterModule.forChild([
		{ path: '', component: FileImportComponent }

	])],
	exports: [RouterModule]
})
export class FileImportRoutingModule { }
