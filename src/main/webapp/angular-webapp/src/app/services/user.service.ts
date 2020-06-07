import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {User} from "../models/User";
import {Observable, throwError} from "rxjs";
import {catchError, retry} from "rxjs/operators";
import {GlobalVariables} from "../models/GlobalVariables";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private BASE_URL: string;

  constructor(private http: HttpClient,
              public globals: GlobalVariables) {
    this.BASE_URL = "http://localhost:8080/api";
  }


  login(credentials: string[]){
    let result = this.http.post<User>(this.BASE_URL + "/login", credentials)
      .pipe(catchError(this.handleError))
      .subscribe((user: User) =>{
        console.log("Output:");
        console.log(user);
        this.globals.user = user;
        //will finish later
    });

    return result;

  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    //return throwError('Something bad happened; please try again later.');
    return new Observable(observer => {
      observer.next("error");
    });
  };

}


