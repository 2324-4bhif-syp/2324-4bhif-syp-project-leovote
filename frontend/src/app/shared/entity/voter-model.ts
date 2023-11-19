export class Voter{
  private _schoolId: string;
  private _password: string;

  constructor(schoolId: string, password: string) {
    this._schoolId = schoolId;
    this._password = password;
  }

  get schoolId(): string {
    return this._schoolId;
  }

  set schoolId(value: string) {
    this._schoolId = value;
  }

  get password(): string {
    return this._password;
  }

  set password(value: string) {
    this._password = value;
  }
}
