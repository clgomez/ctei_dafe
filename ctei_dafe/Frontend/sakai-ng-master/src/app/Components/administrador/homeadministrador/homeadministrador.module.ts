import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeAdministradorRoutingModule } from './homeadministrador-routing.module';
import { HomeAdministradorComponent } from './homeadministrador.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        HomeAdministradorRoutingModule,
        FormsModule
    ],
    declarations: [HomeAdministradorComponent]
})
export class HomeAdministradorModule { }
