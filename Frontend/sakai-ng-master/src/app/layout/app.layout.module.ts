import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { InputTextModule } from 'primeng/inputtext';
import { SidebarModule } from 'primeng/sidebar';
import { BadgeModule } from 'primeng/badge';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputSwitchModule } from 'primeng/inputswitch';
import { RippleModule } from 'primeng/ripple';
import { AppMenuComponent } from './menu/app.menu.component';
import { AppMenuitemComponent } from './menuitem/app.menuitem.component';
import { RouterModule } from '@angular/router';
import { AppTopBarComponent } from './topbar/app.topbar.component';
import { AppFooterComponent } from './footer/app.footer.component';
import { AppConfigModule } from './config/config.module';
import { AppSidebarComponent } from "./sidebar/app.sidebar.component";
import { AppLayoutComponent } from "./app.layout.component";
import { TooltipModule } from 'primeng/tooltip';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { DatePipe } from '@angular/common';
import { PasswordModule } from 'primeng/password';
import { NotificationComponent } from './topbar/notifications/notification.component';
import { ProfileComponent } from './topbar/profile/profile.component'; 
@NgModule({
    declarations: [
        AppMenuitemComponent,
        AppTopBarComponent,
        AppFooterComponent,
        AppMenuComponent,
        AppSidebarComponent,
        AppLayoutComponent,
        NotificationComponent,
        ProfileComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        BrowserAnimationsModule,
        InputTextModule,
        SidebarModule,
        BadgeModule,
        RadioButtonModule,
        InputSwitchModule,
        RippleModule,
        RouterModule,
        AppConfigModule,
        TooltipModule,
        ButtonModule,
        DialogModule,
        PasswordModule 

    ],
    exports: [AppLayoutComponent],
    providers: [DatePipe]
})
export class AppLayoutModule { }
