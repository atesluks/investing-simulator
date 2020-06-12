import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Stock} from "../models/Stock";
import {Observable, of} from "rxjs";
import {catchError, retry, tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class FinancialsService {

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  private BASE_URL: string;

  constructor(private http: HttpClient) {
    this.BASE_URL = "http://localhost:8080/api";
  }

  getAllStocks(): Observable<Map<string, Stock>> {
    return this.http.get<Map<string, Stock>>(this.BASE_URL + "/financials", this.httpOptions)
      .pipe(
        retry(3),
        tap(h => {
          const outcome = h ? `fetched` : `did not find`;
          console.log(`${outcome} financials`);
        }),
        catchError(this.handleError<Map<string, Stock>>(`financials`)));
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
