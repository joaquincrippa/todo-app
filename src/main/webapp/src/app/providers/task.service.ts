import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
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

  query(req?: any): Observable<HttpResponse<Task[]>> {
    const options = this.createRequestOption(req);
    return this.http.get<Task[]>(this.resourceUrl, { params: options, observe: 'response' });
}

private createRequestOption(req?: any): HttpParams {
  let options: HttpParams = new HttpParams();
  if (req) {
      Object.keys(req).forEach(key => {
          if (key !== 'sort') {
              options = options.set(key, req[key]);
          }
      });
      if (req.sort) {
          req.sort.forEach(val => {
              options = options.append('sort', val);
          });
      }
  }
  return options;
};


}
