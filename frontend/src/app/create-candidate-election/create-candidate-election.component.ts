import { Component } from '@angular/core';
import { Candidate } from "../shared/entity/candidate-model";
import { CandidateService } from "../shared/control/candidate.service";
import { LeovoteWebApiService } from "../shared/api/leovote-web-api.service";

@Component({
  selector: 'app-create-candidate-election',
  templateUrl: './create-candidate-election.component.html',
  styleUrls: ['./create-candidate-election.component.css']
})
export class CreateCandidateElectionComponent {
  candidate: Candidate = new Candidate();
  //imageFile: File | null = null;
  constructor(protected candidateService: CandidateService, private apiService: LeovoteWebApiService) { }

  createCandidate() {
    if (this.candidate.firstName && this.candidate.lastName && this.candidate.schoolId) {
      this.candidate.pathOfImage = "default.jpg"
      console.log(this.candidate)
      this.candidateService.add(this.candidate);
    }
  }
  /*
  onImageChange(event: any) {
    this.imageFile = event.target.files[0];
  }*/
}
