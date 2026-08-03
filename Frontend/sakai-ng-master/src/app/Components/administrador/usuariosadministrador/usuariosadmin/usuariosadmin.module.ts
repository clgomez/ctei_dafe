import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuariosAdminRoutingModule } from './usuariosadmin-routing.module';
import { UsuariosAdminComponent } from './usuariosadmin.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';
import { InputSwitchModule } from 'primeng/inputswitch';
import { PasswordModule } from 'primeng/password';
import { DatePipe } from '@angular/common';

@NgModule({
    imports: [
        CommonModule,
        UsuariosAdminRoutingModule, InputSwitchModule, PasswordModule,
        FormsModule, TableModule, ButtonModule, DialogModule, TooltipModule
    ],
    declarations: [UsuariosAdminComponent],
    providers: [DatePipe]
})
export class UsuariosAdminModule { }
