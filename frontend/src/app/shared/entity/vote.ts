export class Vote {
  //<editor-fold desc="Fields">
  generatedId: string;
  participatingIn: number;
  voted: boolean;
  //</editor-fold>

  //<editor-fold desc="Constructors">
  constructor(generatedId: string, participatingIn: number, voted: boolean) {
    this.generatedId = generatedId;
    this.participatingIn = participatingIn;
    this.voted = voted;
  }
}
