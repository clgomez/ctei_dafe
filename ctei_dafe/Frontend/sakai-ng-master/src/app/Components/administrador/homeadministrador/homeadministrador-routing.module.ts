import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HomeAdministradorComponent } from './homeadministrador.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: HomeAdministradorComponent }
    ])],
    exports: [RouterModule]
})
export class HomeAdministradorRoutingModule { }
