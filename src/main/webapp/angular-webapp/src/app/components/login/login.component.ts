import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {GlobalVariables} from "../../models/GlobalVariables";

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

  constructor(private userService: UserService,
              public globals: GlobalVariables) {
    this.alertFadeShow = 'fade';
  }

  ngOnInit() {
  }

  onLogin(event: Event, email: string, password: string) {
    event.preventDefault();

    let credentials = [email, password];

    this.userService.login(credentials);

    this.changeAlert("alert-danger", "Email or password is incorrect");

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
