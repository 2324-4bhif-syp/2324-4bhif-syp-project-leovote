export class CandidateImage {
  //<editor-fold desc="Fields">
  candidateId: number | null;
  imagePath: string;
  //</editor-fold>

  //<editor-fold desc="Constructors">
  constructor(
    candidateId: number | null = null,
    imagePath: string = "",
  ) {
    this.candidateId = candidateId;
    this.imagePath = imagePath
  }
}
