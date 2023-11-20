export class Election {
  //<editor-fold desc="Fields">
  private _id: number | null;
  private _name: string;
  private _electionStart: Date;
  private _electionEnd: Date;
  private _electionType: string;
  //</editor-fold>

  //<editor-fold desc="Constructors">
  constructor(
    id: number | null = null,
    name: string = "",
    electionStart: Date = new Date(),
    electionEnd: Date = new Date(),
    electionType: string
  ) {
    this._id = id;
    this._name = name;
    this._electionStart = electionStart;
    this._electionEnd = electionEnd;
    this._electionType = electionType;
  }

  //</editor-fold>

  //<editor-fold desc="Getter and Setter">
  get id(): number | null {
    return this._id;
  }

  set id(value: number | null) {
    this._id = value;
  }

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get electionStart(): Date {
    return this._electionStart;
  }

  set electionStart(value: Date) {
    this._electionStart = value;
  }

  get electionEnd(): Date {
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

  //</editor-fold>
}
