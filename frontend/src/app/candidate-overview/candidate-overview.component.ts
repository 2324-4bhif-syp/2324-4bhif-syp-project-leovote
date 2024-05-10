import {Component, OnInit} from '@angular/core';
import {Candidate} from "../shared/entity/candidate-model";
import {ElectionService} from "../shared/control/election.service";
import {CandidateService} from "../shared/control/candidate.service";
import {CandidateImage} from "../shared/entity/candidate-image";

@Component({
  selector: 'app-candidate-overview',
  templateUrl: './candidate-overview.component.html',
  styleUrls: ['./candidate-overview.component.css']
})
export class CandidateOverviewComponent implements OnInit {
  candidates: Candidate[] = [];
  candidatesImage : CandidateImage[] = [];

  constructor(public candidateService: CandidateService) {
  }

  ngOnInit() {
    this.candidateService.candidateImage().subscribe(candidateImage => {
      this.candidatesImage = candidateImage;
      this.candidateService.getList().subscribe( candidate=> {
        this.candidates = candidate;
        this.linkCandidateImages();
      })
    })
  }

  linkCandidateImages() {
    if (this.candidates.length > 0) {
      this.candidates.forEach((candidate) => {
        const matchingImage = this.candidatesImage.find((c) => c.candidateId === candidate.id);
        if (matchingImage) {
          candidate.pathOfImage = matchingImage.imagePath;
          console.log(candidate.pathOfImage);
        }
      });
    }
  }
  getBase64Image(base64String: string | undefined): string {
    return `data:image/jpeg;base64,${base64String}`;
  }
}
