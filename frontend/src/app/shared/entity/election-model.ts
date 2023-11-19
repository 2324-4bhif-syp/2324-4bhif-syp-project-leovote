export class Election {
  private _id: string
  private _electionStart: Date
  private _electionEnd: Date
  private _electionType: string

  constructor(id: string, electionStart: Date, electionEnd: Date, electionType: string) {
    this._id = id;
    this._electionStart = electionStart;
    this._electionEnd = electionEnd;
    this._electionType = electionType;
  }

  get id(): string {
    return this._id;
  }

  set id(value: string) {
    this._id = value;
  }

  get electionStart(): Date{
    return this._electionStart;
  }
  set electionStart(value: Date) {
    this._electionStart = value;
  }

  get electionEnd(): Date{
    return this._electionEnd;
  }
  set electionEnd(value: Date) {
    this._electionEnd = value;
  }

  get electionType(): string {
    return this._electionType;
  }

  set electionType(value: string) {
    this._electionType = value;
  }
}
