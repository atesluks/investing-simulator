export class User{
  id:number;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  portfolios: number[];


  constructor(id:number, email: string, firstName: string, lastName: string) {
    this.id=id;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
