import { Component, OnInit } from '@angular/core';
import { TaskService } from 'src/app/providers/task.service';
import { Task } from 'src/app/entities/task';
import { MatDialogRef } from '@angular/material/dialog';
import {MatSnackBar} from '@angular/material'
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-new-task',
  templateUrl: './new-task.component.html',
  styleUrls: ['./new-task.component.css']
})
export class NewTaskComponent implements OnInit {
  newTask = new Task();
  isSaving = false;
  error = false;
  fileName = 'Choose a file';

  constructor(
    private taskService: TaskService,
    private dialogRef: MatDialogRef<NewTaskComponent>,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit() {
  }

  save() {
    this.isSaving = true;
    this.taskService.create(this.newTask).subscribe(
      (res: HttpResponse<Task>) => {
        this.isSaving = false;
        this.snackBar.open('Task created!', 'CLOSE', {
          duration: 2000,
        });
        this.dialogRef.close({created: true});
      },
      (res: HttpErrorResponse) => {
        this.isSaving = false;
        this.error = true;
        setTimeout(() => (this.error = false), 2000);
      }
    );
  }

  onPictureChange(event: any) {
    if (event && event.target.files && event.target.files[0]) {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        const result = reader.result as string;
        this.newTask.picture = result.substr(result.indexOf('base64,') + 7);
        this.newTask.pictureContentType = file.type;
        this.fileName = file.name;
      }
     } else {
        this.newTask.picture = null;
        this.newTask.pictureContentType = null;
      }
    }

    clearPicture() {
      this.newTask.picture = null;
      this.newTask.pictureContentType = null;
      this.fileName = 'Choose a file';
    }
  }
