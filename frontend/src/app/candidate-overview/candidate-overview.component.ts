import {Component, OnInit} from '@angular/core';
import {Candidate} from "../shared/entity/candidate-model";
import {ElectionService} from "../shared/control/election.service";
import {CandidateService} from "../shared/control/candidate.service";
import {CandidateImage} from "../shared/entity/candidate-image";
import {Router} from "@angular/router";
@Component({
  selector: 'app-candidate-overview',
  templateUrl: './candidate-overview.component.html',
  styleUrls: ['./candidate-overview.component.css']
})
export class CandidateOverviewComponent implements OnInit {
  candidates: Candidate[] = [];
  candidatesImage : CandidateImage[] = [];
  searchText: string = "";
  filteredCandidates: Candidate[] = [];

  constructor(public candidateService: CandidateService,
              private router: Router) {
  }

  ngOnInit() {
    this.candidateService.candidateImage().subscribe(candidateImage => {
      this.candidatesImage = candidateImage;
      this.candidateService.getList().subscribe( candidate=> {
        this.candidates = candidate;
        this.linkCandidateImages();
        this.filteredCandidates = this.candidates;
      })
    })
  }

  filterCandidates() {
    if (this.searchText.trim() === '') {
      // If search text is empty, show all candidates
      this.filteredCandidates = this.candidates;
    } else {
      // If search text is not empty, filter candidates based on it
      this.filteredCandidates = this.candidates.filter(candidate =>
        candidate.firstName.toLowerCase().includes(this.searchText.toLowerCase()) ||
        candidate.lastName.toLowerCase().includes(this.searchText.toLowerCase())
      );
    }
  }

  linkCandidateImages() {
    if (this.candidates.length > 0) {
      this.candidates.forEach((candidate) => {
        const matchingImage = this.candidatesImage.find((c) => c.candidateId === candidate.id);
        if (matchingImage) {
          candidate.pathOfImage = matchingImage.imagePath;
          //console.log(candidate.pathOfImage);
        }
      });
    }
  }
  getBase64Image(base64String: string | undefined): string {
    return `data:image/jpeg;base64,${base64String}`;
  }

  deleteCandidate(candidate: Candidate): void {
    if (candidate.id !== null) {
      if(confirm(`Do you want to delete ${candidate.firstName} ${candidate.lastName}`))
      this.candidateService.isCandidateInAnyElection(candidate.id).subscribe(
        isInAnyElection => {
          if (isInAnyElection) {
            alert(`Candidate ${candidate.firstName} ${candidate.lastName} is in one or more elections!`);
          } else {
            if (candidate.id !== null) {
              this.candidateService.delete(candidate.id.toString()).subscribe(
                () => {
                  this.candidates = this.candidates.filter(c => c.id !== candidate.id);
                },
                error => {
                  console.error('Error deleting candidate:', error);
                }
              );
            }
            this.ngOnInit();
          }
        },
        error => {
          console.error('Error checking candidate in any election:', error);
        }
      );
    }
  }
}
