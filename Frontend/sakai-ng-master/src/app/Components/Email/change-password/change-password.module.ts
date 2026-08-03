import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChangePasswordRoutingModule } from './change-password-routing.module';
import { ChangePasswordComponent } from './change-password.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        ChangePasswordRoutingModule,
        FormsModule
    ],
    declarations: [ChangePasswordComponent]
})
export class ChangePasswordModule { }
