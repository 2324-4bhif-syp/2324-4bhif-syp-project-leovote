export class Candidate{
  private _schoolId: string
  private _firstName:string
  private _lastName:string
  private _grade:string


  constructor(schoolId: string, firstName: string, lastName: string, grade: string) {
    this._schoolId = schoolId;
    this._firstName = firstName;
    this._lastName = lastName;
    this._grade = grade;
  }

  get schoolId(): string {
    return this._schoolId;
  }

  set schoolId(value: string) {
    this._schoolId = value;
  }

  get firstName(): string {
    return this._firstName;
  }

  set firstName(value: string) {
    this._firstName = value;
  }

  get lastName(): string {
    return this._lastName;
  }

  set lastName(value: string) {
    this._lastName = value;
  }

  get grade(): string {
    return this._grade;
  }

  set grade(value: string) {
    this._grade = value;
  }
}
