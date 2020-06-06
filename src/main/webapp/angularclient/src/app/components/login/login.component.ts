import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    private credentials: LoginCredentials;

    alertType: string;
    alertText: string;

  constructor(private route: ActivatedRoute,
              private router: Router
  ) { }

  ngOnInit() {
  }

  private onSubmit(event: Event, email: string, password: string){
      this.credentials.email = email;
      this.credentials.password = password;
      //send this info through the service
  }

    goToRegistration(){
        this.router.navigate(['/user-registration']);
    }

}

interface LoginCredentials{
    email: string,
    password: string

}
