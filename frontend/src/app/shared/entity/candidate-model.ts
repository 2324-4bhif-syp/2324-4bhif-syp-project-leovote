export class Candidate {
  //<editor-fold desc="Fields">
  private _id: number | null
  private _schoolId: string
  private _firstName: string
  private _lastName: string
  private _grade: string
  //</editor-fold>

  //<editor-fold desc="Constructors">
  constructor(
    id: number | null = null,
    schoolId: string = "",
    firstName: string = "",
    lastName: string = "",
    grade: string = ""
  ) {
    this._id = id;
    this._schoolId = schoolId;
    this._firstName = firstName;
    this._lastName = lastName;
    this._grade = grade;
  }

  //</editor-fold>

  //<editor-fold desc="Getter and Setter">
  get id(): number | null {
    return this._id;
  }

  set id(value: number | null) {
    this._id = value;
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

  //</editor-fold>
}
