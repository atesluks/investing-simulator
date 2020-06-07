import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user.service";
import {GlobalVariables} from "../../models/GlobalVariables";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
  providers: [GlobalVariables]
})
export class SignupComponent implements OnInit {

  private alertType: string;
  private alertText: string;
  private alertFadeShow: string;

  constructor(private userService: UserService,
              public globals: GlobalVariables) { }

  ngOnInit() {
    this.alertFadeShow = 'fade';
  }

  onSignup(event:Event, email:string, firstName:string, lastName:string, password:string, repeatPassword:string){
    event.preventDefault();

    if (password.length<6 || password.length>20){
      this.changeAlert("alert-danger", "Password should be 6-20 symbols long");
      return;
    }

    if (password != repeatPassword){
      this.changeAlert("alert-danger", "Password confirmation doesn't match the password");
      return;
    }

    this.alertFadeShow = 'fade';




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
