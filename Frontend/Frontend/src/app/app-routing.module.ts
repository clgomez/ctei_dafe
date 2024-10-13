import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { WelcomeComponent } from './Components/welcome/welcome.component';
import { AuthGuard } from './Guards/auth.guard';
import { SendEmailComponent } from './Components/Email/send-email/send-email.component';
import { ChangePassworComponent } from './Components/Email/change-passwor/change-passwor.component';
import { LoginComponent } from './Components/login/login.component';
import { RegisterComponent } from './Components/register/register.component';
import { HomeInvestigadorComponent } from './Components/investigador/homeinvestigador/homeinvestigador.component';


  const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'home', component: WelcomeComponent, canActivate: [AuthGuard] },
    { path: 'change-password/:tokenPassword', component: ChangePassworComponent},
    { path: 'send-email', component: SendEmailComponent},
    { path: 'homeinvestigador', component: HomeInvestigadorComponent, canActivate: [AuthGuard] },
    { path: '**', redirectTo: '/login' }
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
