import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NotificacionesInvestComponent } from './notificacionesinvest.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: NotificacionesInvestComponent }
    ])],
    exports: [RouterModule]
})
export class NotificacionesInvestRoutingModule { }
