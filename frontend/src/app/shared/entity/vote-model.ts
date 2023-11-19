import {Election} from "./election-model";
import {Candidate} from "./candidate-model";

export class Vote{
  candidate: Candidate;
  election: Election;

  constructor(candidate: Candidate, election: Election) {
    this.election = election;
    this.candidate = candidate;
  }
}
