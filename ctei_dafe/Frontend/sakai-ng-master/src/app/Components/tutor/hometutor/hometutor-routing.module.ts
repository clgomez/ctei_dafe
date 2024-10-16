import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HomeTutorComponent } from './hometutor.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: '', component: HomeTutorComponent }
    ])],
    exports: [RouterModule]
})
export class HomeTutorRoutingModule { }
