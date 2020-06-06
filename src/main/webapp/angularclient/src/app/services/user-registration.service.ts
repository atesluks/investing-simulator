import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {User} from "../model/user";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class UserRegistrationService {

    constructor(private http: HttpClient) { }

    addUser(user: User): Observable<User>{
        return this.http.post<User>('http://localhost:8080/api/users/', user);//.pipe(map(data => new Boolean(data)));
    }
}
