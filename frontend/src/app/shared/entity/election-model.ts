export class Election {
  public id: number | null
  public name: string
  public  electionStart: Date
  public electionEnd: Date
  public electionType: string

  constructor(id: number | null, name: string, electionStart: Date, electionEnd: Date, electionType: string) {
    this.id = id;
    this.name = name;
    this.electionStart = electionStart;
    this.electionEnd = electionEnd;
    this.electionType = electionType;
  }
}
