import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../../models/User";
import {UserService} from "../../services/user.service";
import {Portfolio} from "../../models/Portfolio";
import {Deal} from "../../models/Deal";
import {Stock} from "../../models/Stock";
import {GraphComponent} from "../graph/graph.component";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrls: ['./portfolio.component.css']
})
export class PortfolioComponent implements OnInit {

  @ViewChild(GraphComponent) theGraphComponent: GraphComponent;

  public portfolioId: number;
  public theUser: User;
  public thePortfolio: Portfolio;
  public openDeals: Array<Deal>;
  public closedDeals: Array<Deal>;
  public selectedDeal: Deal;
  public allStocks: Map<String, Stock>;

  //alert related variables
  public alertType: string;
  public alertText: string;
  public alertShowFade: string;

  constructor(private _route: ActivatedRoute,
              private router: Router,
              private userService: UserService,
              private cookies: CookieService) {
  }

  ngOnInit() {
    let userCookies = this.cookies.get('/user');
    console.log("PORTFOLIO PAGE COOKIES:");
    console.log(this.cookies.get('/user'));
    if (userCookies == "") {
      this.router.navigate(['/login']);
      return;
    }

    this.theUser = JSON.parse(userCookies);
    this.updateUser(this.theUser.id);

    this.portfolioId = +this._route.snapshot.paramMap.get('id');

    this.thePortfolio = new Portfolio("", 0, 0);
    this.getPortfolio(this.portfolioId);

    //default values
    this.openDeals = new Array<Deal>();
    this.closedDeals = new Array<Deal>();
    this.selectedDeal = new Deal("", 0, 0);
    this.allStocks = new Map<String, Stock>();

    this.alertType = "alert-success";
    this.alertText = "";
    this.alertShowFade = "fade";
  }

  getPortfolio(id: number) {
    this.userService.getPortfolio(id).subscribe((portfolio: Portfolio) => {

      this.thePortfolio = portfolio;
      this.getAllDeals();
    });
  }

  getAllStocks($event: any) {
    this.allStocks = $event.stocks;
  }

  getAllDeals() {
    this.openDeals = new Array<Deal>();
    this.closedDeals = new Array<Deal>();

    this.thePortfolio.deals.forEach((id) => {
      this.userService.getDeal(id).subscribe((theDeal: Deal) => {
        if (theDeal.closingPrice == null) {
          this.openDeals.push(theDeal);
        } else {
          this.closedDeals.push(theDeal);
        }

        //the arrays will be sorted when all deals will be retrieved
        if((this.openDeals.length + this.closedDeals.length) == this.thePortfolio.deals.length){
          this.openDeals = this.openDeals.sort((t1, t2) => {
            const name1 = t1.id
            const name2 = t2.id;
            if (name1 > name2) { return -1; }
            if (name1 < name2) { return 1; }
            return 0;
          });
          this.closedDeals = this.closedDeals.sort((t1, t2) => {
            const name1 = t1.id;
            const name2 = t2.id;
            if (name1 > name2) { return -1; }
            if (name1 < name2) { return 1; }
            return 0;
          });
        }

      });
    });
  }

  sellStock(deal: Deal){
    this.userService.closeDeal(deal.id).subscribe((deal: Deal)=>{
      document.getElementById("closeModal").click();
      this.getPortfolio(this.portfolioId);
      this.alertType="alert-success";
      this.alertText=`${this.roundNumbers(deal.amount)} of ${deal.stockSymbol} stocks were successfuly sold for 
        $${this.roundNumbers(deal.closingPrice)} per stock`;
      this.alertShowFade="show";
    });
  }

  updateUser(id: number){
    this.userService.getUser(id).subscribe((result: User) => {
      if (result==undefined){
        this.cookies.set('/user',"");
        this.router.navigate(['/login']);
      }else {
        this.cookies.set('/user', JSON.stringify(result));
        this.theUser = result;

        if (!this.theUser.portfolios.includes(this.portfolioId)) {
          this.router.navigate(['/']);
        }
      }
    });
  }

  //updates portfolio and shows alert that the stock was purchased successfully
  buyStockEventHandler($event: any) {
    this.getPortfolio(this.portfolioId);

    let theDeal = $event.theDeal;
    this.alertShowFade = "show";
    this.alertType="alert-success";
    this.alertText=`${this.roundNumbers(theDeal.amount)} of ${theDeal.stockSymbol} 
      stocks were successfully bought for $${this.roundNumbers(theDeal.openPrice)} per stock`;
  }

  calculateProfit(){
    let price =+ this.allStocks.get(this.selectedDeal.stockSymbol).price;
    let profit = this.roundNumbers(this.selectedDeal.amount * (price - this.selectedDeal.openPrice));
    let profitString = '';
    if(profit<0){
      profitString = (profit+'').replace('-','-$');
    }else{
      profitString = '$'+profit;
    }
    return profitString;
  }

  showStockOnChart($event: any){
    console.log("Stock selected for chart:");
    let theStock: Stock;
    theStock = $event.stock;
    console.log(theStock);

    if (theStock.timeSeries.length == 0){
      //if time series data for the stock is not available yet, we will show an alert
      this.alertText = `Time series data for stock ${theStock.symbol} is not available yet. Please, try again later`;
      this.alertType = "alert-warning";
      this.alertShowFade = "show";
    }else{
      //pass the data to graph component
      this.theGraphComponent.updateGraph(theStock);
    }
  }

  public roundNumbers(num: any){
    let theNum =+ num;
    return Math.round((theNum + Number.EPSILON) * 100) / 100;
  }

  public formatTime(time : string): string{
    let theTime = time;
    theTime = theTime.substring(0, theTime.length-3);
    theTime = theTime.replace('T',' at ');
    return theTime;
  }

  public calculateClosedProfit(theDeal: Deal): string{
    let profit = this.roundNumbers(theDeal.amount * (theDeal.closingPrice - theDeal.openPrice));
    let profitString = '';
    if(profit<0){
      profitString = (profit+'').replace('-','-$');
    }else{
      profitString = '$'+profit;
    }
    return profitString;
  }

  public calcualteClsoedProfitPercents(theDeal: Deal): string{
    return this.roundNumbers((theDeal.closingPrice - theDeal.openPrice)/theDeal.openPrice * 100)+'%';
  }

  public stringToNum(theString: any): number{
    let theNumber =+ theString;
    return theNumber;
  }

  public formatNumbers(num: number) {
    let result = num + '';
    if (result.indexOf('.') == -1) {
      result = result + '.00';
    } else if (result.charAt(result.length - 2) == '.') {
      result = result + '0';
    }
    result = result.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return result;
  }

}
