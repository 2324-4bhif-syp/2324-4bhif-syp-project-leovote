export class EmailModel {
  //<editor-fold desc="Fields">
  email: string;
  id: number;
  electionId: number;
  //</editor-fold>

  //<editor-fold desc="Constructors">
  constructor(
    email: string, id: number, electionId: number
  ) {
    this.email = email;
    this.id = id;
    this.electionId = electionId;
  }
}
