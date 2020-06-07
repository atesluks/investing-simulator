import { Component, OnInit } from '@angular/core';
import {User} from "../../models/User";
import {GlobalVariables} from "../../models/GlobalVariables";

@Component({
  selector: 'app-mainpage',
  templateUrl: './mainpage.component.html',
  styleUrls: ['./mainpage.component.css']
})
export class MainpageComponent implements OnInit {

  theUser:User;

  constructor(private globalVariables: GlobalVariables) {
    this.theUser = globalVariables.user;
    console.log("From the main component:");
    console.log(this.theUser);
  }

  ngOnInit() {
  }

}
