import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../../models/User";
import {Cookie} from "ng2-cookies/ng2-cookies";
import {UserService} from "../../services/user.service";
import {Portfolio} from "../../models/Portfolio";
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

  constructor(private _route: ActivatedRoute,
              private router: Router,
              private userService: UserService) { }

  ngOnInit() {
    this.portfolioId =+ this._route.snapshot.paramMap.get('id');
    console.log("Portfolio's id from the link:");
    console.log(this.portfolioId);

    this.theUser = JSON.parse(Cookie.get('user'));

    if (!this.theUser.portfolios.includes(this.portfolioId)){
      this.router.navigate(['/']);
    }

    this.thePortfolio = new Portfolio("",0,0);
    this.getPortfolio(this.portfolioId);
  }

  getPortfolio(id: number){
    this.userService.getPortfolio(id).subscribe((portfolio: Portfolio)=> {
      console.log(`portfolioComponent returned portfolio (id=${id}):`);
      console.log(portfolio);

      this.thePortfolio = portfolio;
    });
  }

  buyStockEventHandler($event: any){
    this.getPortfolio(this.portfolioId);
  }

}
