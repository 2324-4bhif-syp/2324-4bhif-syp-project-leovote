import {CandidateVoteDto} from "./candidate-vote-dto";

export class VoteRequestDto {
  //<editor-fold desc="Fields">
  voterId: string;
  candidateVotes: CandidateVoteDto[]
  //</editor-fold>

  //<editor-fold desc="Constructors">
  constructor(generatedId: string, candidateVoteDTO: CandidateVoteDto[]) {
    this.voterId = generatedId;
    this.candidateVotes = candidateVoteDTO;
  }
}
