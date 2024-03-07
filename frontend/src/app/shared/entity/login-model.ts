export class LoginModel {
  sub: string;
  emailVerified: boolean;
  lDapEntry: string;
  name: string;
  preferredUsername: string;
  firstName: string;
  familyName: string;

  constructor(sub: string, emailVerified: boolean, lDapEntry: string, name: string, preferredUsername: string, firstName: string, familyName: string) {
    this.sub = sub;
    this.emailVerified = emailVerified;
    this.lDapEntry = lDapEntry;
    this.name = name;
    this.preferredUsername = preferredUsername;
    this.firstName = firstName;
    this.familyName = familyName;
  }
}
