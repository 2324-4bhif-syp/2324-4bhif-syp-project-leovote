import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Candidate} from "../shared/entity/candidate-model";

@Component({
  selector: 'app-candidate',
  templateUrl: './candidate.component.html',
  styleUrls: ['./candidate.component.css']
})
export class CandidateComponent {
  @Input() doEdit: boolean = false;
  @Input() candidate: Candidate = new Candidate(null, "", "", "", "");
  createCandidate: Candidate = new Candidate(null, "", "", "", "");
  @Output() editDone = new EventEmitter<Candidate>();

  create(){
    this.doEdit = false;
    this.editDone.emit(this.createCandidate);
    this.candidate = this.createCandidate;
    this.createCandidate = new Candidate(null, "", "", "", "");
  }
}
