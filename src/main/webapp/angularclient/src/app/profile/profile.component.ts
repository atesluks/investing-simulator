import { Component, OnInit } from '@angular/core';
import {User} from "../shared/model/user";
import {UserService} from "../shared/user/user.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

    private theUser: User;
    private oldPasswordValue:string;
    private newPasswordValue1:string;
    private newPasswordValue2:string;


    private updateProfileAlertType:string;
    private updateProfileAlertMessage:string;
    private changePasswordAlertType: string;
    private changePasswordAlertMessage:string;


    constructor(private userService: UserService) { }

    ngOnInit() {
        this.theUser = new User();

        this.userService.getUser(2).subscribe((response: User) =>{
            console.log(response);
            this.theUser = response;
        })
    }

    private profileSaved(event: Event){
        event.preventDefault();
    }

    private passwordChanged(event: Event, oldPass: string, newPass1: string, newPass2: string){
        event.preventDefault();

        if (newPass1.length<8){
            this.changePasswordAlert("alert-danger","Your password must be 8-20 characters long");
            return;
        }

        if (newPass1 !== newPass2){
            this.changePasswordAlert("alert-danger","Password confirmation doesn't match the password");
            return;
        }


        this.userService.getMatchPassword(2, oldPass).subscribe((response: boolean) =>{
            if (response == false){
                this.changePasswordAlert("alert-danger","Old password isn't valid");
                return;
            }else{
                //changing password
            }

        });



    }

    private changePasswordAlert(alertType:string, message:string){
        this.changePasswordAlertType = alertType;
        this.changePasswordAlertMessage = message;
    }

}
