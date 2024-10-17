import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/Guards/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'change-password/:tokenPassword', loadChildren: () => import('./change-password/change-password.module').then(m => m.ChangePasswordModule) },
        { path: 'send-email', loadChildren: () => import('./send-email/send-email.module').then(m => m.SendEmailModule) },
        { path: '**', redirectTo: '/login' }
    ])],
    exports: [RouterModule]
})
export class EmailRoutingModule { }
