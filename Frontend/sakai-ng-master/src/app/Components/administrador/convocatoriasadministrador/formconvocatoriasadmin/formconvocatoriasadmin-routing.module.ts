import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormConvocatoriasAdminComponent } from './formconvocatoriasadmin.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: FormConvocatoriasAdminComponent }
    ])],
    exports: [RouterModule]
})
export class FormConvocatoriasAdminRoutingModule { }
