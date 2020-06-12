import { Component, OnInit } from '@angular/core';
import {Stock} from "../../../models/Stock";
import {FinancialsService} from "../../../services/financials.service";

@Component({
  selector: 'app-all-stocks-list',
  templateUrl: './all-stocks-list.component.html',
  styleUrls: ['./all-stocks-list.component.css']
})
export class AllStocksListComponent implements OnInit {

  private allStocks: Map<String, Stock>;

  constructor(private financialsService: FinancialsService) {
    this.allStocks = new Map<String, Stock>();
  }

  ngOnInit() {
    this.financialsService.getAllStocks().subscribe(result=>{

      let map = new Map<string, Stock>();
      for (var value in result) {
        map.set(value,result[value])
      }

      this.allStocks = map;
    });
  }

}
