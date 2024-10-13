import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ConvocatoriasAdministradorComponent } from './convocatoriasadministrador.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ConvocatoriasAdministradorComponent }
    ])],
    exports: [RouterModule]
})
export class ConvocatoriasAdministradorRoutingModule { }
