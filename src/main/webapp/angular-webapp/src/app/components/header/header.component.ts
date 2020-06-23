import { Component, OnInit } from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private cookies: CookieService,
              private router: Router) { }

  ngOnInit(): void {
  }

  public logout(){
    this.cookies.set('/user','');
    this.router.navigate(['/login']);
  }

}
