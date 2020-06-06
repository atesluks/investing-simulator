import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserRegistrationComponent} from "./components/user-registration/user-registration.component";
import { LoginComponent} from "./components/login/login.component";


const routes: Routes = [
    { path: 'user-registration', component: UserRegistrationComponent},
    { path: 'login', component: LoginComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
