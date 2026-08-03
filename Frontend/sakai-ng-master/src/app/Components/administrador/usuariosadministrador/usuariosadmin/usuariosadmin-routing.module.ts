import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UsuariosAdminComponent } from './usuariosadmin.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: UsuariosAdminComponent }
    ])],
    exports: [RouterModule]
})
export class UsuariosAdminRoutingModule { }
