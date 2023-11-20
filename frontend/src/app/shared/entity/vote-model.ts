import {Election} from "./election-model";
import {Candidate} from "./candidate-model";

export class Vote {
  //<editor-fold desc="Fields">
  private _candidate: Candidate;
  private _election: Election;
  //</editor-fold>

  //<editor-fold desc="Constructors">
  constructor(candidate: Candidate, election: Election) {
    this._election = election;
    this._candidate = candidate;
  }

  //</editor-fold>

  //<editor-fold desc="Getter and Setter">
  get candidate(): Candidate {
    return this._candidate;
  }

  set candidate(value: Candidate) {
    this._candidate = value;
  }

  get election(): Election {
    return this._election;
  }

  set election(value: Election) {
    this._election = value;
  }

  //</editor-fold>
}
