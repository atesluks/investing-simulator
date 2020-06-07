import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {GlobalVariables} from "../../models/GlobalVariables";
import {User} from "../../models/User";
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [GlobalVariables]
})
export class LoginComponent implements OnInit {

  private alertType: string;
  private alertText: string;
  private alertFadeShow: string;
  private spinnerFadeShow: string;

  constructor(private userService: UserService,
              public globals: GlobalVariables,
              private router: Router) {
    this.alertFadeShow = 'fade';
    this.spinnerFadeShow = 'fade';
  }

  ngOnInit() {
  }

  onLogin(event: Event, email: string, password: string) {
    event.preventDefault();

    this.spinnerFadeShow = 'show';

    let credentials = [email, password];

    this.userService.login(credentials).subscribe((result: User)=>{
      this.globals.user = result;

      this.spinnerFadeShow = 'fade';

      if (this.globals.user == undefined){
        this.changeAlert("alert-danger", "Email or password is incorrect");
      }else{
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

  hideAlert(){
    this.alertFadeShow = "fade";
  }


}
