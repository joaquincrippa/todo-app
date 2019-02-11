import { Component, OnInit } from '@angular/core';
import { TaskService } from 'src/app/providers/task.service';
import { Task } from 'src/app/entities/task';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { NewTaskComponent } from '../../components/new-task/new-task.component';
import { MatSnackBar } from '@angular/material';
import { TaskStatus } from 'src/app/entities/task-status.enum';

@Component({
  selector: 'app-list-tasks',
  templateUrl: './list-tasks.component.html',
  styleUrls: ['./list-tasks.component.css']
})
export class ListTasksComponent implements OnInit {
  tasks: Task[] = [];
  page: IPage = { page: 0, size: 10, sort: ['id,desc'] };
  filter: IFilter = { id: null, status: null, description: null };
  links: any;
  totalItems = 0;
  totalPages = 0;
  selectedTask = null;
  isLoading = false;
  filterHidden = true;
  statusOptions = Object.keys(TaskStatus);

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
        .query({...this.page, ...this.filter})
        .subscribe(
            (res: HttpResponse<Task[]>) => {
              this.totalItems = parseInt(res.headers.get('total-items'), 10);
              this.totalPages = parseInt(res.headers.get('total-pages'), 10);
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
    if (!this.isLoading && this.page.page < this.totalPages) {
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
      if (result && result.created) {
        this.reset();
      }
    });
  }

  markAsResolved(id: number, index: number) {
    this.taskService.partiallyUpdate({id: id, status: TaskStatus.RESOLVED}).subscribe(
      (res: HttpResponse<Task>) => {
        this.tasks[index] = res.body;
      },
      (res: HttpErrorResponse) => {
        this.snackBar.open('Oops! Something went wrong. Try again later!', '', {
          duration: 4000,
        });
      }
    );
  }

  changeFilterVisibility() {
    this.filterHidden = !this.filterHidden;
    this.filter = { id: null, status: null, description: null };
  }

  search() {
    this.page.page = 0;
    this.tasks = [];
    this.loadAll();
  }
}

interface IPage {
  page: number;
  size: number;
  sort: any;
}

interface IFilter {
  id: number;
  status: TaskStatus;
  description: String;
}


