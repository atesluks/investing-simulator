import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import {UserService} from "./services/user.service";
import { MainpageComponent } from './components/mainpage/mainpage.component';
import { PortfolioListComponent } from './components/mainpage/portfolio-list/portfolio-list.component';
import { AllStocksListComponent } from './components/all-stocks-list/all-stocks-list.component';
import {FormsModule} from "@angular/forms";
import {FinancialsService} from "./services/financials.service";
import { PortfolioComponent } from './components/portfolio/portfolio.component';

import { ChartModule, StockChartAllModule, ChartAllModule } from '@syncfusion/ej2-angular-charts';
import { DateTimeService, CategoryService, LegendService, CandleSeriesService, TooltipService, RangeTooltipService, PeriodSelectorService} from '@syncfusion/ej2-angular-charts';
import {GraphComponent} from "./components/graph/graph.component";
import {CookieService} from "ngx-cookie-service";
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';

const appRoutes: Routes = [
  {path:'', component:MainpageComponent},
  {path:'login', component:LoginComponent},
  {path:'signup', component:SignupComponent},
  {path:'portfolio/:id', component:PortfolioComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    MainpageComponent,
    PortfolioListComponent,
    AllStocksListComponent,
    PortfolioComponent,
    GraphComponent,
    HeaderComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule.forRoot(appRoutes),
    HttpClientModule,
    FormsModule,
    ChartModule,
    StockChartAllModule,
    ChartAllModule
  ],
  providers: [
    UserService,
    FinancialsService,
    CategoryService,
    LegendService,
    CandleSeriesService,
    CookieService,
    TooltipService,
    RangeTooltipService,
    PeriodSelectorService,
    DateTimeService],
  bootstrap: [AppComponent]
})
export class AppModule { }
