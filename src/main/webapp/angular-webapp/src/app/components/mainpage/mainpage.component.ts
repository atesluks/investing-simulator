import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import * as $ from 'jquery';
import {Router} from "@angular/router";
import {Cookie} from 'ng2-cookies/ng2-cookies';
import {User} from "../../models/User";

@Component({
  selector: 'app-mainpage',
  templateUrl: './mainpage.component.html',
  styleUrls: ['./mainpage.component.css']
})
export class MainpageComponent implements OnInit {

  private theUser: User;

  constructor(private userService: UserService,
              private router: Router) {

    this.theUser = JSON.parse(Cookie.get('user'));
    console.log("Cookies.theUser (in main page):");
    console.log(this.theUser);

    if (this.theUser == undefined) {
      this.router.navigate(['/login']);
    }
  }

  ngOnInit() {

  }


}
