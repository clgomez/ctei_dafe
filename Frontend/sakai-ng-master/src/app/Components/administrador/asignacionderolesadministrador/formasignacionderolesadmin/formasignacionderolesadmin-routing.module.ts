import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormAsignacionDeRolesAdminComponent } from './formasignacionderolesadmin.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: FormAsignacionDeRolesAdminComponent }
    ])],
    exports: [RouterModule]
})
export class FormAsignacionDeRolesAdminRoutingModule { }

