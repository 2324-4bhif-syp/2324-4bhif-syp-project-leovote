import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Candidate} from "../shared/entity/candidate-model";
import {CandidateService} from "../shared/control/candidate.service";
import {CandidateImage} from "../shared/entity/candidate-image";

@Component({
  selector: 'app-candidate-update',
  templateUrl: './candidate-update.component.html',
  styleUrls: ['./candidate-update.component.css']
})
export class CandidateUpdateComponent implements OnInit {
  candidateId: number | null = null;
  candidate: Candidate = new Candidate();
  candidateImage: CandidateImage | null = null;
  updatedCandidate: Candidate = new Candidate();
  imageFile: File | null = null;

  constructor(
    private candidateService: CandidateService,
    private route: ActivatedRoute,
    private router: Router) {
  }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      const id = params.get('candidate');
      if (id != null && Number(id)) {
        this.candidateId = Number(id);
        this.candidateService.getById(this.candidateId).subscribe(candidate => {
          this.candidate = candidate;
          this.updatedCandidate = new Candidate();
          this.updatedCandidate.firstName = this.candidate.firstName;
          this.updatedCandidate.lastName = this.candidate.lastName;
          this.updatedCandidate.grade = this.candidate.grade;
          this.updatedCandidate.id = this.candidate.id;
          this.updatedCandidate.schoolId = this.candidate.schoolId;
          this.updatedCandidate.pathOfImage = this.candidate.pathOfImage;
          this.updatedCandidate.schoolId = this.candidate.schoolId;
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

  onImageChange(event: any) {
    this.imageFile = event.target.files[0];
  }

  resetImageInput() {
    const imageInput = document.getElementById('imageInput') as HTMLInputElement;
    if (imageInput) {
      imageInput.value = '';
    }
    this.imageFile = null;
  }

  linkCandidateImages() {
    if (this.candidateImage != null && this.candidate != null) {
      this.candidate.pathOfImage = this.candidateImage.imagePath;
    }
  }

  getBase64Image(base64String: string | undefined): string {
    return `data:image/jpeg;base64,${base64String}`;
  }

  updateCandidate() {
    if (this.candidateId != null) {
      if (this.imageFile) {
        let fileExtension: string = this.imageFile.name.split(".").pop()!;
        // Erstelle den neuen Dateinamen durch Verknüpfen von schoolId und Dateiendung
        let newFileName: string = `${this.updatedCandidate.schoolId}.${fileExtension}`;
        this.candidate.pathOfImage = newFileName;
        console.log("HIER: " + newFileName)
        console.log(this.updatedCandidate.schoolId);
        this.candidateService.update(this.updatedCandidate, this.candidateId).subscribe(
          (response) => {
            console.log('Candidate updated successfully:', response);
          },
          (error) => {
            console.error('Error updating candidate:', error);
          }
        );
        this.candidateService.uploadImage(this.imageFile, this.candidate.schoolId).subscribe(
          (response: string) => {
            console.log('Bild erfolgreich hochgeladen:', response);
          },
          (error) => {
            console.error('Fehler beim Hochladen des Bildes:', error);
          }
        );
        this.resetImageInput();
        this.router.navigate(['/admin', "overview", "candidate-overview"]);
      } else {

      }
    }
  }

  checkFields() {
    return this.updatedCandidate.firstName == "" || this.updatedCandidate.lastName == "" || this.updatedCandidate.grade == ""
      || this.updatedCandidate.schoolId == null;
  }
}
