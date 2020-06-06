export class User {
    id: number;
    email: string;
    firstName: string;
    lastName: string;
    password: string;

    constructor(email: string, firstName: string, lastName: string, password: string) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}
