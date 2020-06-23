import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {UserService} from "../../services/user.service";
import * as $ from 'jquery';
import {Router} from "@angular/router";
import {User} from "../../models/User";
import {Stock} from "../../models/Stock";
import {GraphComponent} from "../graph/graph.component";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-mainpage',
  templateUrl: './mainpage.component.html',
  styleUrls: ['./mainpage.component.css']
})
export class MainpageComponent implements OnInit {

  @ViewChild(GraphComponent) theGraphComponent: GraphComponent;

  constructor(private cookies: CookieService,
              private router: Router,
              private elementRef: ElementRef) {
  }

  ngOnInit() {

  }

  showStockOnChart($event: any){
    console.log("Stock selected for chart:");
    let theStock: Stock;
    theStock = $event.stock;
    console.log(theStock);

    if (theStock.timeSeries.length == 0){
      //if time series data for the stock is not available yet, we will show an alert
      // this.alertText = `Time series data for stock ${theStock.symbol} is not available yet. Please, try again later`;
      // this.alertType = "alert-warning";
      // this.alertShowFade = "show";
    }else{
      //pass the data to graph component
      this.theGraphComponent.updateGraph(theStock);
    }
  }

  ngAfterViewInit(){
    this.elementRef.nativeElement.ownerDocument.body.style.backgroundColor = '#ffffff';
  }

}
