export class Election {
  id: string
  start: Date
  end: Date
  electionType: string


  constructor(id: string, start: Date, end: Date, electionType: string) {
    this.id = id;
    this.start = start;
    this.end = end;
    this.electionType = electionType;
  }
}
