import { Component } from '@angular/core';
import {CandidateService} from "../shared/control/candidate.service";
import {Candidate} from "../shared/entity/candidate-model";

@Component({
  selector: 'app-candidate',
  templateUrl: './candidate.component.html',
  styleUrls: ['./candidate.component.css']
})
export class CandidateComponent {
  private candidateService: CandidateService;
  protected candidates: Candidate[];
  protected newCandidate: Candidate;
  constructor(candidateService: CandidateService) {
    this.candidateService = candidateService;
    this.candidates = [];
    this.initCandidate();
    this.newCandidate = {
      schoolId: "",
      firstName: "",
      lastName: "",
      grade: ""
    }
  }
  async initCandidate() {
    this.candidates = await this.candidateService.getCandidates();
    console.debug(this.candidates);
  }
  async add() {
    await this.candidateService.createCandidate(this.newCandidate);
    this.newCandidate = {
      schoolId:"",
      firstName:"",
      lastName: "",
      grade:""
    }
  }
}
