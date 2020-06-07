import { Component, OnInit } from '@angular/core';
import {Stock} from "../../../models/Stock";
import {FinancialsService} from "../../../services/financials.service";

@Component({
  selector: 'app-all-stocks-list',
  templateUrl: './all-stocks-list.component.html',
  styleUrls: ['./all-stocks-list.component.css']
})
export class AllStocksListComponent implements OnInit {

  allStocks: Stock[];

  constructor(private financialsService: FinancialsService) { }

  ngOnInit() {

  }

}
