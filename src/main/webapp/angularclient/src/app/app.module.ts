import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HttpClientModule} from "@angular/common/http";
import { ProfileComponent } from './components/profile/profile.component';
import { UserRegistrationComponent } from './components/user-registration/user-registration.component';
import {FormsModule} from "@angular/forms";
import { LoginComponent } from './components/login/login.component';
import {RouterModule, Routes} from "@angular/router";

const appRoutes: Routes = [
    {path:'', component: LoginComponent},
    {path:'login', component: LoginComponent},
    {path:'signup', component:UserRegistrationComponent},
    {path:'profile', component:ProfileComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    ProfileComponent,
    UserRegistrationComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
      HttpClientModule,
      FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
