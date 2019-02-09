import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ListTasksComponent } from '../app/components/list-tasks/list-tasks.component';

const routes: Routes = [
  {
    path: 'tasks',
    component: ListTasksComponent
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
