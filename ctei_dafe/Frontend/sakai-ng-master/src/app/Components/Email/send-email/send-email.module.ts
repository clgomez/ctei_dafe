import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SendEmailRoutingModule } from './send-email-routing.module';
import { SendEmailComponent } from './send-email.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        SendEmailRoutingModule,
        FormsModule
    ],
    declarations: [SendEmailComponent]
})
export class SendEmailModule { }
