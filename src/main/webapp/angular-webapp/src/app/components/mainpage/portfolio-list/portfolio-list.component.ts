import { Component, OnInit } from '@angular/core';
import {UserService} from "../../../services/user.service";
import {User} from "../../../models/User";
import {Cookie} from "ng2-cookies/ng2-cookies";
import {Portfolio} from "../../../models/Portfolio";
import {Router} from "@angular/router";

@Component({
  selector: 'app-portfolio-list',
  templateUrl: './portfolio-list.component.html',
  styleUrls: ['./portfolio-list.component.css']
})
export class PortfolioListComponent implements OnInit {

  private theUser: User;
  private allPortfolios;
  private createPortfolioAlertText;
  private createPortfolioAlertHideShow;
  private createPortfolioAlertType;
  private spinnerFadeShow;
  private portfolioToDelete: Portfolio;

  constructor(private userService: UserService,
              private router: Router) { }

  ngOnInit() {
    this.theUser = JSON.parse(Cookie.get('user'));
    this.getPortfolios();
    this.createPortfolioAlertHideShow = "fade";
    this.createPortfolioAlertText = "Portfolio with this name already exists! Chose another name!";
    this.createPortfolioAlertType = "alert-danger";
    this.spinnerFadeShow= "fade";
    this.portfolioToDelete = new Portfolio("",0,0);
  }

  getPortfolios(){
    let ids = this.theUser.portfolios;
    this.allPortfolios = new Array<Portfolio>();

    console.log("User:");
    console.log(this.theUser);

    console.log("Ids:");
    console.log(ids);

    ids.forEach((id) => {
      this.userService.getPortfolio(id).subscribe((portfolio: Portfolio)=> {
        console.log(`portfolio-list returned portfolio (id=${id}):`);
        console.log(portfolio);

        this.allPortfolios.push(portfolio);

        console.log("protfolio list:");
        console.log(this.allPortfolios);

      });
    });

  }

  onCreateNewPortfolio(event: Event, name: string, initialInvestment: string){
    event.preventDefault();

    this.spinnerFadeShow= "show";

    let investmentNumber = +initialInvestment;
    let newPortfolio = new Portfolio(name, investmentNumber, this.theUser.id);

    console.log("New portfolio:");
    console.log(newPortfolio);

    this.userService.saveNewPortfolio(newPortfolio).subscribe((result: Portfolio)=>{

      console.log("SavePortfolio returned portfolio:");
      console.log(result);

      this.spinnerFadeShow = 'fade';

      if (result == undefined){
        this.createPortfolioAlertType = "alert-danger";
        this.createPortfolioAlertText = "Portfolio with this name already exists! Chose another name!";
        this.createPortfolioAlertHideShow = "show";
      }else{
        this.createPortfolioAlertType = "alert-success";
        this.createPortfolioAlertText = `Portfolio ${name} created. Updating...`;

        this.spinnerFadeShow = 'show';

        this.updateUser(this.theUser.id);
      }
    });
  }

  updateUser(id: number){
    this.userService.getUser(id).subscribe((result: User) => {
      if (result==undefined){
        Cookie.set('user',undefined);
        this.router.navigate(['/login']);
      }else{
        Cookie.set('user',JSON.stringify(result));
        this.theUser = result;
        this.getPortfolios();

        this.spinnerFadeShow = 'fade';
        //closing the modal window
        document.getElementById("closeModalButton").click();
        document.getElementById("closeDeleteModal").click();
      }

    });
  }

  deletePortfolio(portfolio: Portfolio){
    this.userService.deletePortfolio(portfolio.id).subscribe((result: string) => {
      console.log("Response of the deletePortfolio method:");
      console.log(result);
      this.updateUser(this.theUser.id);
    });
  }


  setPortfolioToDelete(portfolio: Portfolio){
    this.portfolioToDelete = portfolio;
  }

  onModalClosed(){
    this.spinnerFadeShow="fade";
    this.createPortfolioAlertHideShow="fade";
    document.getElementById("closeModalButton").click();
  }

}
