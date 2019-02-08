import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Task } from '../entities/task';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  public resourceUrl = environment.api + 'tasks';

  constructor(protected http: HttpClient) { }

  create(task: Task): Observable<HttpResponse<Task>> {
    return this.http.post<Task>(this.resourceUrl, task, { observe: 'response' });
  }

}
