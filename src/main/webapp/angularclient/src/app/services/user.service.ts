import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../model/user";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<User[]>{
        return this.http.get<User[]>('http://localhost:8080/api/users/');
  }

    getUser(id: number): Observable<User>{
        return this.http.get<User>('http://localhost:8080/api/users/'+id);
    }

    getMatchPassword(id: number, password: string): Observable<boolean>{
      return this.http.post<boolean>('http://localhost:8080/api/users/'+id, password);//.pipe(map(data => new Boolean(data)));
    }


}
