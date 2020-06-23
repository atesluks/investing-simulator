import {Component, ElementRef, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {User} from "../../models/User";
import {Router} from '@angular/router';
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public alertType: string;
  public alertText: string;
  public alertFadeShow: string;
  public spinnerFadeShow: string;

  constructor(private userService: UserService,
              private router: Router,
              private elementRef: ElementRef,
              private cookies: CookieService) {

    if (this.cookies.get('/user') != "") {
      this.router.navigate(['/']);
    }
  }

  ngOnInit() {
    this.alertFadeShow = 'fade';
    this.spinnerFadeShow = 'fade';
  }

  onLogin(event: Event, email: string, password: string) {
    event.preventDefault();

    this.spinnerFadeShow = 'show';

    let credentials = [email, password];

    this.userService.login(credentials).subscribe((result: User)=>{

      console.log("Login returned user:");
      console.log(result);

      this.spinnerFadeShow = 'fade';

      if (result == undefined){
        this.changeAlert("alert-danger", "Email or password is incorrect");
      }else{
        this.cookies.set('/user',JSON.stringify(result));
        this.alertFadeShow = "fade";
        //go to the main page
        this.router.navigate(['/']);
      }
    });
  }

  changeAlert(alertType:string, alertText:string){
    this.alertFadeShow = "show";
    this.alertType = alertType;
    this.alertText = alertText;
  }

  ngAfterViewInit(){
    this.elementRef.nativeElement.ownerDocument.body.style.backgroundColor = '#022140';
  }

}
