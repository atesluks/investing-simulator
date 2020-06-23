import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {Stock} from "../../models/Stock";
import {PeriodsModel} from "@syncfusion/ej2-charts";

@Component({
  selector: 'app-graph',
  templateUrl: './graph.component.html',
  styleUrls: ['./graph.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class GraphComponent implements OnInit {

  public stockchartData: Object[];
  public title: string;
  public tooltip: Object;
  public crosshair: any;

  constructor() {
  }

  ngOnInit(): void {
    this.crosshair = { enable: true };
    this.tooltip = { enable: true, header: this.title, format: '<b>${point.x} : ${point.y}</b>' };
  }

  updateGraph(stock: Stock){
    this.title = `${stock.symbol} Historical Price`;
    this.stockchartData = stock.timeSeries;

  }



}
