import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {User} from "../models/User";
import {Observable, throwError, of} from "rxjs";
import {catchError, map, retry, tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  private BASE_URL: string;

  constructor(private http: HttpClient) {
    this.BASE_URL = "http://localhost:8080/api";
  }

  login(credentials: string[]): Observable<User>{
    return this.http.post<User>(this.BASE_URL + "/login", credentials, this.httpOptions)
      .pipe(
        //map(users => users[0]), // returns a {0|1} element array
        tap(h => {
          const outcome = h ? `fetched` : `did not find`;
          console.log(`${outcome} user`);
        }),
        catchError(this.handleError<User>(`login email=${credentials[0]}`)));
  }

  signup(user: User): Observable<User>{
    return this.http.post<User>(this.BASE_URL + "/users", user, this.httpOptions)
      .pipe(
        tap(h => {
          const outcome = h ? `fetched` : `did not find`;
          console.log(`${outcome} user`);
        }),
        catchError(this.handleError<User>(`login email=${user.email}`)));
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error); // log to console instead
      console.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

}
