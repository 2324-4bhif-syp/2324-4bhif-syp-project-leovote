import {Component, OnInit} from '@angular/core';
import {Election} from "../shared/entity/election-model";
import {ActivatedRoute, Router} from "@angular/router";
import {Candidate} from "../shared/entity/candidate-model";
import {CandidateService} from "../shared/control/candidate.service";
import {CandidateImage} from "../shared/entity/candidate-image";

@Component({
  selector: 'app-candidate-update',
  templateUrl: './candidate-update.component.html',
  styleUrls: ['./candidate-update.component.css']
})
export class CandidateUpdateComponent {
  candidateId: number | null = null;
  candidate: Candidate = new Candidate();
  candidateImage: CandidateImage | null = null;
  updatedCandidate: Candidate = new Candidate();

  constructor(
    private candidateService: CandidateService,
    private route: ActivatedRoute,
    private router: Router) {
    this.route.queryParamMap.subscribe(params => {
      const id = params.get('candidate');
      if (id != null && Number(id)) {
        this.candidateId = Number(id);
        candidateService.getById(this.candidateId).subscribe(candidate => {
          this.candidate = candidate;
          if (this.candidateId != null) {
            this.candidateService.getImageById(this.candidateId).subscribe(candidateImage => {
              this.candidateImage = candidateImage;
              this.linkCandidateImages();
            });
          }
        });
      }
    });
  }

  linkCandidateImages() {
    if (this.candidateImage != null && this.candidate != null) {
      this.candidate.pathOfImage = this.candidateImage.imagePath;
    }
  }

  getBase64Image(base64String: string | undefined): string {
    return `data:image/jpeg;base64,${base64String}`;
  }

  onImageChange($event: Event) {

  }

  updateCandidate() {
    if (this.candidateId != null) {
      this.candidateService.update(this.updatedCandidate, this.candidateId).subscribe(() => {
        this.router.navigate(['/admin', "overview", "candidate-overview"]);
      });
    }
  }
}
