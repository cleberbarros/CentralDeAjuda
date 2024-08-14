export enum Role {
    MANAGER = "MANAGER",
    USER = "USER",
  }
  
  export class User {
    constructor(
      public id: number,
      public email: string,
      public name: string,
      public roles: Role[]
    ) {}
  }
  