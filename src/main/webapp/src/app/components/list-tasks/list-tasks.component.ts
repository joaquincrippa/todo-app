import { Component, OnInit } from '@angular/core';
import { TaskService } from 'src/app/providers/task.service';
import { Task } from 'src/app/entities/task';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { NewTaskComponent } from '../../components/new-task/new-task.component';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-list-tasks',
  templateUrl: './list-tasks.component.html',
  styleUrls: ['./list-tasks.component.css']
})
export class ListTasksComponent implements OnInit {
  tasks: Task[] = [];
  page: IPage = {page: 0, size: 10, sort: ['id,desc']};
  links: any;
  totalItems = 0;
  totalPages = 0;
  selectedTask = null;
  isLoading = false;

  constructor(
    private taskService: TaskService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit() {
    this.loadAll();
  }

  loadAll() {
    this.isLoading = true;
    this.taskService
        .query(this.page)
        .subscribe(
            (res: HttpResponse<Task[]>) => {
              this.totalItems = parseInt(res.headers.get('total-items'));
              this.totalPages = parseInt(res.headers.get('total-pages'));
              this.tasks = this.tasks.concat(res.body);
              this.isLoading = false;
            },
            (res: HttpErrorResponse) => {
              this.snackBar.open('Oops! Something went wrong. Try again later!', '', {
                duration: 4000,
              });
              this.isLoading = false;
            }
        );
  }

  nextPage() {
    console.log('otra page');
    if(!this.isLoading && this.page.page < this.totalPages) {
      this.page.page ++;
      this.loadAll();  
    }
  }

  reset() {
    this.page.page = 0;
    this.tasks = [];
    this.loadAll();
  }

  showPicture(task: Task, templateRef: any) {
    this.selectedTask = task;
    this.dialog.open(templateRef, {
    });
  }

  showNewTask(): void {
    const dialogRef = this.dialog.open(NewTaskComponent, {
      width: '350px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result.created) {
        this.reset();
      }
    });
  }
}

interface IPage { 
  page: number, 
  size: number,
  sort: any
}


