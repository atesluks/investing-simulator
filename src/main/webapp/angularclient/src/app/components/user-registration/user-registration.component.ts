import { Component, OnInit } from '@angular/core';
import {User} from "../../model/user";
import {UserService} from "../../services/user.service";
import {UserRegistrationService} from "../../services/user-registration.service";

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent implements OnInit {


    private theUser: User;

    private alertType: string;
    private alertText:string;

    private email: string="";
    private firstName: string="";
    private lastName:string="";
    private password1:string="";
    private password2: string="";

    constructor(private userRegistrationService: UserRegistrationService) { }

    ngOnInit() {
    }

    private onSubmit(event: Event, email: string, firstName: string, lastName:string, password1:string, password2: string){
        event.preventDefault();

        if (password1.length < 8 || password1.length > 20) {
            this.changeAlert("alert-danger", "Your password must be 8-20 characters long");
            return;
        }

        if (password1 !== password2) {
            this.changeAlert("alert-danger", "Password confirmation doesn't match the password");
            return;
        }

        let user = new User(email, firstName, lastName, password1);

        this.userRegistrationService.addUser(user).subscribe(response =>{


        });

    }

    private changeAlert(alertType:string, message:string){
        this.alertType = alertType;
        this.alertText = message;
    }

}
