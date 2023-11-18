import {Component} from '@angular/core';
import {CandidateService} from "../shared/control/candidate.service";
import {Candidate} from "../shared/entity/candidate-model";

@Component({
  selector: 'app-candidate-list',
  templateUrl: './candidate-list.component.html',
  styleUrls: ['./candidate-list.component.css']
})
export class CandidateListComponent {
  protected candidates: Candidate[] = [];
  protected newCandidate: Candidate = new Candidate("", "", "", "");

  private initNewCandidate() {
    this.newCandidate = new Candidate("", "", "", "");
  }

  constructor(public CS: CandidateService) {

  }

  async initCandidates() {
    this.candidates = await this.CS.getCandidates();
    console.debug(this.candidates);
  }

  async clearCandidates() {
    this.candidates = [];
    console.debug(this.candidates);
  }

  async add() {
    await this.CS.createCandidate(this.newCandidate);
    this.initNewCandidate();
  }
}
