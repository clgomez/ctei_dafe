import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ProyectosAdminComponent } from './proyectosadmin.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: ProyectosAdminComponent }
    ])],
    exports: [RouterModule]
})
export class ProyectosAdminRoutingModule { }
