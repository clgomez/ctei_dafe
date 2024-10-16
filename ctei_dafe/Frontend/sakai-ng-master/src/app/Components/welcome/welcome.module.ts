import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WelcomeRoutingModule } from './welcome-routing.module';
import { WelcomeComponent } from './welcome.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        WelcomeRoutingModule,
        FormsModule
    ],
    declarations: [WelcomeComponent]
})
export class WelcomeModule { }
