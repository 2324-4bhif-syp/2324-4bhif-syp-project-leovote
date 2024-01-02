import { Candidate } from "./candidate-model";

export class Election {
  //<editor-fold desc="Fields">
  id: number | null;
  name: string;
  electionStart: Date;
  electionEnd: Date;
  electionType: string;
  blockchainFileName: string;
  participatingCandidates: Candidate[];
  //</editor-fold>

  //<editor-fold desc="Constructors">
  constructor(
    id: number | null = null,
    name: string = "",
    electionStart: Date = new Date(),
    electionEnd: Date = new Date(),
    electionType: string,
    blockchainFileName: string,
    participatingCandidates: Candidate[]
  ) {
    this.id = id;
    this.name = name;
    this.electionStart = electionStart;
    this.electionEnd = electionEnd;
    this.electionType = electionType;
    this.blockchainFileName = blockchainFileName;
    this.participatingCandidates = participatingCandidates;
  }
}
