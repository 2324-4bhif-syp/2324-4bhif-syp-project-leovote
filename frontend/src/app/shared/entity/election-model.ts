export class Election {
  //<editor-fold desc="Fields">
  id: number | null;
  name: string;
  electionStart: Date;
  electionEnd: Date;
  electionType: string;
  //</editor-fold>

  //<editor-fold desc="Constructors">
  constructor(
    id: number | null = null,
    name: string = "",
    electionStart: Date = new Date(),
    electionEnd: Date = new Date(),
    electionType: string
  ) {
    this.id = id;
    this.name = name;
    this.electionStart = electionStart;
    this.electionEnd = electionEnd;
    this.electionType = electionType;
  }
}
