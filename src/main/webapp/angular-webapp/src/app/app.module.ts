import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import {UserService} from "./services/user.service";
import {GlobalVariables} from "./models/GlobalVariables";
import { MainpageComponent } from './components/mainpage/mainpage.component';
import { PortfolioListComponent } from './components/mainpage/portfolio-list/portfolio-list.component';
import { AllStocksListComponent } from './components/mainpage/all-stocks-list/all-stocks-list.component';
import { GraphComponent } from './components/mainpage/graph/graph.component';
import { LatestDealsComponent } from './components/mainpage/latest-deals/latest-deals.component';
import {FormsModule} from "@angular/forms";

const appRoutes: Routes = [
  {path:'', component:MainpageComponent},
  {path:'login', component:LoginComponent},
  {path:'signup', component:SignupComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    MainpageComponent,
    PortfolioListComponent,
    AllStocksListComponent,
    GraphComponent,
    LatestDealsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule.forRoot(appRoutes),
    HttpClientModule,
    FormsModule
  ],
  providers: [UserService, GlobalVariables],
  bootstrap: [AppComponent]
})
export class AppModule { }
