import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { AppLayoutComponent } from "./layout/app.layout.component";
import { AuthGuard } from './Guards/auth.guard';

@NgModule({
    imports: [
        RouterModule.forRoot([
            {
                path: '', component: AppLayoutComponent,
                children: [
                    //{ path: '', loadChildren: () => import('./demo/components/dashboard/dashboard.module').then(m => m.DashboardModule) },
                    //{ path: 'documentation', loadChildren: () => import('./demo/components/documentation/documentation.module').then(m => m.DocumentationModule) },
                    { path: '', loadChildren: () => import('./Components/welcome/welcome.module').then(m => m.WelcomeModule) },
                    { path: 'welcome', loadChildren: () => import('./Components/welcome/welcome.module').then(m => m.WelcomeModule) },
                    { path: 'login', loadChildren: () => import('./Components/login/login.module').then(m => m.LoginModule) },
                    { path: 'register', loadChildren: () => import('./Components/register/register.module').then(m => m.RegisterModule) },
                    { path: 'email', loadChildren: () => import('./Components/Email/email.module').then(m => m.EmailModule) },
                    { path: 'investigador', loadChildren: () => import('./Components/investigador/investigador.module').then(m => m.InvestigadorModule) },
                    { path: 'tutor', loadChildren: () => import('./Components/tutor/tutor.module').then(m => m.TutorModule) },
                    { path: 'evaluador', loadChildren: () => import('./Components/evaluador/evaluador.module').then(m => m.EvaluadorModule) },
                    { path: 'administrador', loadChildren: () => import('./Components/administrador/administrador.module').then(m => m.AdministradorModule) }
                ]
            },

            { path: '**', redirectTo: '/login' },
        ], { scrollPositionRestoration: 'enabled', anchorScrolling: 'enabled', onSameUrlNavigation: 'reload' })
    ],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
