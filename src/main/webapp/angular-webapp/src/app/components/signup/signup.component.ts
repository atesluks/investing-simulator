import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user.service";
import {GlobalVariables} from "../../models/GlobalVariables";
import {User} from "../../models/User";
import {Router} from "@angular/router";
import {NgForm} from '@angular/forms';

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
  private spinnerFadeShow: string;

  constructor(private userService: UserService,
              public globals: GlobalVariables,
              private router: Router) { }

  ngOnInit() {
    this.alertFadeShow = 'fade';
    this.spinnerFadeShow = "fade";
  }

  onSignup(event:Event, email:string, firstName:string, lastName:string, password:string, repeatedPassword: string){
    event.preventDefault();

    if (password.length<6 || password.length>20){
      this.changeAlert("alert-danger", "Password should be 6-20 symbols long");
      return;
    }

    if (password != repeatedPassword){
      this.changeAlert("alert-danger", "Password confirmation doesn't match the password");
      return;
    }

    this.spinnerFadeShow = "show";

    let user = new User(0, email,firstName,lastName);
    user.password = password;

    this.userService.signup(user).subscribe((result: User)=>{
      this.globals.user = result;

      console.log("Returned user:");
      console.log(result instanceof User);
      console.log(User);

      this.spinnerFadeShow = 'fade';

      if (this.globals.user == undefined){
        this.changeAlert("alert-danger", "User with this email already exists");
      }else{
        this.alertFadeShow = "fade";
        //go to the main page
        this.router.navigate(['/']);
      }
    });

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
