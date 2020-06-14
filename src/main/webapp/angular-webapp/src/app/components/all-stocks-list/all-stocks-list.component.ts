import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {Stock} from "../../models/Stock";
import {FinancialsService} from "../../services/financials.service";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../services/user.service";
import {Portfolio} from "../../models/Portfolio";
import {Deal} from "../../models/Deal";

@Component({
  selector: 'app-all-stocks-list',
  templateUrl: './all-stocks-list.component.html',
  styleUrls: ['./all-stocks-list.component.css']
})
export class AllStocksListComponent implements OnInit {

  @Output() buyStockEvent = new EventEmitter();
  @Output() getAllStocksEvent = new EventEmitter();
  @Input() thePortfolio: Portfolio;

  public allStocks: Map<String, Stock>;
  private showBuySellButtons: string;
  private buttonDisabled: string;
  private portfolioId: number;
  private selectedStock: Stock;

  //alert and spinners in modal
  private alertType: string;
  private alertText: string;
  private alertHideShow: string;
  private spinnerFadeShow: string;

  constructor(private _route: ActivatedRoute,
              private financialsService: FinancialsService,
              private userService: UserService) {
  }

  ngOnInit() {
    //set default values
    this.allStocks = new Map<String, Stock>();
    this.showBuySellButtons = "fade";
    this.buttonDisabled = "disabled";
    this.alertType = "alert-danger";
    this.alertHideShow = "fade";
    this.spinnerFadeShow = "fade";
    this.portfolioId = 0;
    this.selectedStock = new Stock("", "");

    //if this component is inside portfolio-component, then we
    //retrieve information about the portfolio
    this.portfolioId = +this._route.snapshot.paramMap.get('id');
    if (this.portfolioId > 0) {
      this.showBuySellButtons = "show";
      this.buttonDisabled = "";
    }

    this.getAllStocks();
  }

  onBuyStock(stock: Stock, amount: string) {

    let numberOfStocks = +amount;
    let stockPrice = +stock.price;

    if (this.thePortfolio.cash < stockPrice * numberOfStocks) {
      this.alertText = "You do not have enough cash! Your cash: " + this.thePortfolio.cash;
      this.alertHideShow = "show";
      this.alertType = "alert-danger";
      return;
    }

    this.buyStocks(stock, numberOfStocks);

  }

  //when modal window is closed
  onModalClosed() {
    this.spinnerFadeShow = "fade";
    this.alertHideShow = "fade";
    document.getElementById("closeModalButton").click();
  }

  //updates information about all stocks
  getAllStocks() {
    this.financialsService.getAllStocks().subscribe(result => {

      let map = new Map<string, Stock>();
      for (var value in result) {
        let stock: Stock;
        stock = result[value];
        let stockPrice = "" + stock.price;

        if (stockPrice != 'null') {
          if (stockPrice.indexOf('.') == -1) {
            stock.price = stock.price + '.00';
          } else if (stockPrice.charAt(stockPrice.length - 2) == '.') {
            stock.price = stockPrice + '0';
          }
        }

        map.set(value, stock);
      }

      this.allStocks = map;
      this.getAllStocksEvent.emit({stocks: map});
    });
  }

  buyStocks(stock: Stock, numberOfStocks: number) {
    this.spinnerFadeShow = "show";
    let newDeal = new Deal(stock.symbol, numberOfStocks, this.thePortfolio.id);
    newDeal.openPrice = +stock.price;

    this.userService.addDeal(newDeal).subscribe((returnedDeal: Deal) => {
      if (returnedDeal == undefined) {
        console.log("Something went wrong when saving new deal");
        this.alertHideShow = "show";
        this.alertType = "alert-danger";
        this.alertText = "Something went wrong. Please, try again later";
        this.spinnerFadeShow = "fade";
        return;
      } else {
        this.alertHideShow = "show";
        this.alertType = "alert-success";
        this.alertText = "Stocks were bought";
        this.spinnerFadeShow = "fade";
        this.buyStockEvent.emit({theDeal: newDeal});
        this.onModalClosed();
      }

    });
  }

}
