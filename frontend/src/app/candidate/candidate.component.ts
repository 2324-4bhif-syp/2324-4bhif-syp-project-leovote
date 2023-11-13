import { Component } from '@angular/core';
import {CandidateService} from "../shared/control/candidate.service";
import {Candidate} from "../shared/entity/candidate-model";

@Component({
  selector: 'app-candidate',
  templateUrl: './candidate.component.html',
  styleUrls: ['./candidate.component.css']
})
export class CandidateComponent {
  protected candidates: Candidate[];
  protected newCandidate: Candidate;
  constructor(public CS: CandidateService) {
    this.CS = CS;
    this.candidates = [];
    this.initCandidates();
    this.newCandidate = {
      schoolId: "",
      firstName: "",
      lastName: "",
      grade: ""
    }
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
    this.newCandidate = {
      schoolId:"",
      firstName:"",
      lastName: "",
      grade:""
    }
  }
}
