import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../../models/User";
import {Cookie} from "ng2-cookies/ng2-cookies";
import {UserService} from "../../services/user.service";
import {Portfolio} from "../../models/Portfolio";
import {Deal} from "../../models/Deal";
import {Stock} from "../../models/Stock";

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrls: ['./portfolio.component.css']
})
export class PortfolioComponent implements OnInit {

  private portfolioId: number;
  private theUser: User;
  private thePortfolio: Portfolio;
  private openDeals: Array<Deal>;
  private closedDeals: Array<Deal>;
  private selectedDeal: Deal;
  private allStocks: Map<String, Stock>;

  //alert related variables
  private alertType: string;
  private alertText: string;
  private alertShowFade: string;

  constructor(private _route: ActivatedRoute,
              private router: Router,
              private userService: UserService) {
  }

  ngOnInit() {
    let userCookies = Cookie.get('user');
    if (userCookies == null) {
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
            if (name1 > name2) { return 1; }
            if (name1 < name2) { return -1; }
            return 0;
          });
          this.closedDeals = this.closedDeals.sort((t1, t2) => {
            const name1 = t1.id
            const name2 = t2.id;
            if (name1 > name2) { return 1; }
            if (name1 < name2) { return -1; }
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
      this.alertText=`${deal.amount} of ${deal.stockSymbol} stocks were successfuly sold for $${deal.closingPrice} per stock`;
      this.alertShowFade="show";
    });
  }

  updateUser(id: number){
    this.userService.getUser(id).subscribe((result: User) => {
      if (result==undefined){
        Cookie.set('user',undefined);
        this.router.navigate(['/login']);
      }else {
        Cookie.set('user', JSON.stringify(result));
        this.theUser = result;

        if (!this.theUser.portfolios.includes(this.portfolioId)) {
          this.router.navigate(['/']);
        }
      }
    });
  }

  buyStockEventHandler($event: any) {
    this.getPortfolio(this.portfolioId);

    let theDeal = $event.theDeal;
    this.alertShowFade = "show";
    this.alertType="alert-success";
    this.alertText=`${theDeal.amount} of ${theDeal.stockSymbol} stocks were successfully bought for $${theDeal.openPrice} per stock`;
  }

}
