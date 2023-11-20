import {Election} from "./election-model";
import {Candidate} from "./candidate-model";

export class Vote {
  //<editor-fold desc="Fields">
  candidate: Candidate;
  election: Election;
  //</editor-fold>

  //<editor-fold desc="Constructors">
  constructor(candidate: Candidate, election: Election) {
    this.election = election;
    this.candidate = candidate;
  }
}
