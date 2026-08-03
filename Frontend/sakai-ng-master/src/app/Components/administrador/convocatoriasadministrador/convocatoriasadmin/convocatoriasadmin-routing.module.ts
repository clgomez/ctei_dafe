import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ConvocatoriasAdminComponent } from './convocatoriasadmin.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ConvocatoriasAdminComponent }
    ])],
    exports: [RouterModule]
})
export class ConvocatoriasAdminRoutingModule { }
