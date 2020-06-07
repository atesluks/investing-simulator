import { Injectable } from "@angular/core";
import {User} from "./User";

@Injectable()
export class GlobalVariables{

  user: User = new User(-1,"def", "def", "def");

}
