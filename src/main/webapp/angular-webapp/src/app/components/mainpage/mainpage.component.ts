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

  constructor() {
  }

  ngOnInit() {

  }



}
