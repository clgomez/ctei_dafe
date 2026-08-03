import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module'; 
import { AppLayoutModule } from './layout/app.layout.module'; 
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './Interceptors/auth.interceptor'; 
import { ErrorInterceptor } from './Interceptors/error.interceptor';

@NgModule({
    declarations: [AppComponent],
    imports: [HttpClientModule, AppRoutingModule, AppLayoutModule],
    providers: [
       { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
       { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
    ],
    bootstrap: [AppComponent],
})
export class AppModule {}
