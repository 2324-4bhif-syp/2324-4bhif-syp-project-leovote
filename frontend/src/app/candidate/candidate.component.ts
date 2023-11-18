import { Component } from '@angular/core';
import {Candidate} from "../shared/entity/candidate-model";

@Component({
  selector: 'app-candidate',
  templateUrl: './candidate.component.html',
  styleUrls: ['./candidate.component.css']
})
export class CandidateComponent {
  candidate:Candidate =  new Candidate("if00", "John", "Doe", "0AHIF");
}
