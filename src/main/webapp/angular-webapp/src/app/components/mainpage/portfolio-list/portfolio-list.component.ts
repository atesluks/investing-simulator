import { Component, OnInit } from '@angular/core';
import {UserService} from "../../../services/user.service";
import {User} from "../../../models/User";
import {Portfolio} from "../../../models/Portfolio";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-portfolio-list',
  templateUrl: './portfolio-list.component.html',
  styleUrls: ['./portfolio-list.component.css']
})
export class PortfolioListComponent implements OnInit {

  public theUser: User;
  public allPortfolios: Array<Portfolio>;
  public createPortfolioAlertText;
  public createPortfolioAlertHideShow;
  public createPortfolioAlertType;
  public spinnerFadeShow;
  public portfolioToDelete: Portfolio;

  constructor(private userService: UserService,
              private router: Router,
              private cookies: CookieService) { }

  ngOnInit() {

    let userCookies = this.cookies.get('/user');

    if (userCookies==""){
      this.router.navigate(['/login']);
      return;
    }

    this.theUser = JSON.parse(userCookies);

    this.updateUser(this.theUser.id);

    this.createPortfolioAlertHideShow = "fade";
    this.createPortfolioAlertText = "";
    this.createPortfolioAlertType = "alert-danger";
    this.spinnerFadeShow= "fade";
    this.portfolioToDelete = new Portfolio("",0,0);
  }

  getPortfolios(){
    let ids = this.theUser.portfolios;
    this.allPortfolios = new Array<Portfolio>();

    ids.forEach((id) => {
      this.userService.getPortfolio(id).subscribe((portfolio: Portfolio)=> {

        console.log("GETTING PORTFOLIO. PORTFOLIO:");
        console.log(portfolio);

        this.allPortfolios.push(portfolio);

        //the array will be sorted when all portfolios will be retrieved
        if(this.allPortfolios.length == ids.length){
          this.allPortfolios = this.allPortfolios.sort((t1, t2) => {
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

  onCreateNewPortfolio(event: Event, name: string, initialInvestment: string){
    event.preventDefault();

    this.spinnerFadeShow= "show";

    let investmentNumber = +initialInvestment;
    let newPortfolio = new Portfolio(name, investmentNumber, this.theUser.id);

    this.userService.saveNewPortfolio(newPortfolio).subscribe((result: Portfolio)=>{

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
        this.cookies.set('/user',"");
        this.router.navigate(['/login']);
      }else{
        this.cookies.set('/user',JSON.stringify(result));
        this.theUser = result;
        this.getPortfolios();

        this.spinnerFadeShow = 'fade';
        //closing the modal windows if they are opened
        document.getElementById("closeModalButton").click();
        document.getElementById("closeDeleteModal").click();
      }

    });
  }

  deletePortfolio(portfolio: Portfolio){
    this.userService.deletePortfolio(portfolio.id).subscribe((result: string) => {
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

  public roundNumbers(num: number){
    return Math.round((num + Number.EPSILON) * 100) / 100;
  }

  public goToPortfolio(portfolioId: number){
    this.router.navigate(['/portfolio/'+portfolioId]);
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
