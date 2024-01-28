import { Component } from '@angular/core';
import {Candidate} from "../shared/entity/candidate-model";
import {CandidateService} from "../shared/control/candidate.service";

@Component({
  selector: 'app-create-candidate',
  templateUrl: './create-candidate.component.html',
  styleUrls: ['./create-candidate.component.css']
})
export class CreateCandidateComponent {
  candidate: Candidate = new Candidate();
  //imageFile: File | null = null;
  constructor(protected candidateService: CandidateService) { }

  createCandidate() {
    this.candidate.pathOfImage = "default.jpg";
    console.log(this.candidate)
    this.candidateService.add(this.candidate).subscribe(
      (response) => {
        console.log('Candidate created successfully:', response);
      },
      (error) => {
        console.error('Error creating candidate:', error);
      }
    );
    this.candidate = new Candidate();
  }
  /*
  onImageChange(event: any) {
    this.imageFile = event.target.files[0];
  }*/
}
