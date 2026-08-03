import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FileImportComponent } from './file-import.component';
import { FileImportRoutingModule } from './file-import-routing.module';
import { FormsModule } from '@angular/forms';
import { FileUploadModule } from 'primeng/fileupload';

@NgModule({
	imports: [
		CommonModule,
        FileImportRoutingModule,
		FormsModule,
		FileUploadModule
	],
	declarations: [FileImportComponent]
})
export class FileImportModule { }
