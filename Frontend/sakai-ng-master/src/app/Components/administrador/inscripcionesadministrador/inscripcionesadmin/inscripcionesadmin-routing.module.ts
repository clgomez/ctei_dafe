import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { InscripcionesAdminComponent } from './inscripcionesadmin.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: InscripcionesAdminComponent }
    ])],
    exports: [RouterModule]
})
export class InscripcionesAdminRoutingModule { }
