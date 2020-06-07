import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {GlobalVariables} from "../models/GlobalVariables";
import {Stock} from "../models/Stock";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class FinancialsService {

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  private BASE_URL: string;

  constructor(private http: HttpClient,
              public globals: GlobalVariables) {
    this.BASE_URL = "http://localhost:8080/api";
  }

  getAllStocks(): Observable<Stock[]> {
    return this.http.get<Stock[]>(this.BASE_URL+"/financials");
  }

}
