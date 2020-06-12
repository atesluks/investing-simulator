import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Stock} from "../../../models/Stock";
import {FinancialsService} from "../../../services/financials.service";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../../services/user.service";
import {Portfolio} from "../../../models/Portfolio";

@Component({
  selector: 'app-all-stocks-list',
  templateUrl: './all-stocks-list.component.html',
  styleUrls: ['./all-stocks-list.component.css']
})
export class AllStocksListComponent implements OnInit {

  @Output() buyStockEvent = new EventEmitter<Stock>();

  private allStocks: Map<String, Stock>;
  private showBuySellButtons: string;
  private portfolioId:number;
  private thePortfolio: Portfolio;
  private selectedStock: Stock;

  //alert and spinners in modal
  private alertType: string;
  private alertText: string;
  private alertHideShow: string;
  private spinnerFadeShow: string;

  constructor(private _route: ActivatedRoute,
              private financialsService: FinancialsService,
              private userService: UserService) { }

  ngOnInit() {
    //set default values
    this.allStocks = new Map<String, Stock>();
    this.showBuySellButtons = "fade";
    this.alertType = "alert-danger";
    this.alertHideShow = "fade";
    this.spinnerFadeShow = "fade";
    this.portfolioId = 0;

    //if this component is inside portfolio-component, then we
    //retrieve information about the portfolio
    this.portfolioId =+ this._route.snapshot.paramMap.get('id');
    if (this.portfolioId>0){
      this.showBuySellButtons = "show";
      this.getPortfolio(this.portfolioId);
    }

    this.getAllStocks();
  }

  onBuyStock(stock: Stock, amount: string){

    let numberOfStocks =+ amount;
    let stockPrice =+ stock.price;

    if (this.thePortfolio.cash < stockPrice * numberOfStocks){
      this.alertText="You do not have enough cash! Your cash: "+this.thePortfolio.cash;
      this.alertHideShow="show";
      console.log("slertHideShow");
      console.log(this.alertHideShow);
      return;
    }

    this.buyStocks(stock, numberOfStocks);

  }

  //when modal window is closed
  onModalClosed(){
    this.spinnerFadeShow="fade";
    this.alertHideShow="fade";
    document.getElementById("closeModalButton").click();
  }

  //updates information about the portfolio
  getPortfolio(id: number){
    this.userService.getPortfolio(id).subscribe((portfolio: Portfolio)=> {
      this.thePortfolio = portfolio;
    });
  }

  //updates information about all stocks
  getAllStocks(){
    this.financialsService.getAllStocks().subscribe(result=>{

      let map = new Map<string, Stock>();
      for (var value in result) {
        map.set(value,result[value])
      }

      this.allStocks = map;
      console.log("All stocks:");console.log(map);
    });
  }

  buyStocks(stock: Stock, numberOfStocks: number){
    this.spinnerFadeShow="show";
    this.buyStockEvent.emit();
    this.spinnerFadeShow="fade";
    this.onModalClosed();
  }

}
