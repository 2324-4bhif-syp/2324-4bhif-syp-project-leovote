import {Component} from '@angular/core';
import {Candidate} from "../shared/entity/candidate-model";
import {CandidateService} from "../shared/control/candidate.service";
import {TranslateService} from '@ngx-translate/core';
import {TablePagination} from "../shared/service/TablePaginationService";

@Component({
  selector: 'app-create-candidate',
  templateUrl: './create-candidate.component.html',
  styleUrls: ['./create-candidate.component.css']
})
export class CreateCandidateComponent {
  imageFile: File | null = null;
  fileName: string | null = null;
  candidate: Candidate = new Candidate();
  isCsvUploaded: boolean = false;
  csvData: string = "";
  candidates: Candidate[] = [];
  errorMessage: string = '';
  sampleCandidates = [
    {name: "Adam", class: "1CHIF"},
    {name: "Something", class: "1BHIF"},
    {name: "Alan", class: "3CHIF"},
    {name: "Echo", class: "4BHIF"},
    {name: "Charlie", class: "2CHIF"},
    // Add more rows here as needed
  ];

  tablePaginationService = new TablePagination(this.sampleCandidates, 10)

  constructor(protected candidateService: CandidateService, private translate: TranslateService) {
    candidateService.getList().forEach(value => {
      this.candidates = value;
      this.tablePaginationService = new TablePagination(this.candidates, 10)
    });
  }

  onImageChange(event: any) {
    this.imageFile = event.target.files[0];
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    const reader = new FileReader();
    this.fileName = file.name;
    reader.onload = () => {
      const fileType = file.name.split('.').pop()?.toLowerCase();
      if (fileType !== 'csv') {
        console.error('File is not an CSV type!');
        event.target.value = '';
        return;
      }
      this.isCsvUploaded = true;
      this.csvData = reader.result?.toString() || '';
    };
    reader.readAsText(file);
  }

  parseCSV() {
    const rows = this.csvData.split('\n');
    rows.shift();
    for (const row of rows) {
      const [schoolId, firstName, lastName, grade, pathOfImage] = row.split(';');
      if (schoolId && firstName && lastName && grade) {
        console.log(pathOfImage)
        const candidate = new Candidate(schoolId, firstName, lastName, grade, pathOfImage);
        if (!pathOfImage) {
          candidate.pathOfImage = "default.jpg";
        }
        this.candidate = candidate;
        this.createCandidate();
        console.log(this.candidate);
      } else {
        console.error('CSV data is not in the expected format for the row:', row);
      }
    }
  }

  resetImageInput() {
    const imageInput = document.getElementById('imageInput') as HTMLInputElement;
    if (imageInput) {
      imageInput.value = '';
    }
    this.imageFile = null;
  }

  createCandidate() {
    if (this.imageFile) {
      this.candidateService.uploadImage(this.imageFile, this.candidate.schoolId).subscribe(
        (response: string) => {
          console.log('Bild erfolgreich hochgeladen:', response);
        },
        (error) => {
          console.error('Fehler beim Hochladen des Bildes:', error);
        }
      );
      if (this.imageFile != null) {
        this.candidate.pathOfImage = this.candidate.schoolId + ".jpg";
      } else {
        if (this.candidate.pathOfImage === '') {
          this.candidate.pathOfImage = 'default.jpg';
        }
      }
      this.addCandidate();
    } else {
      if (this.candidate.pathOfImage === '') {
        this.candidate.pathOfImage = 'default.jpg';
      }
      this.addCandidate();
    }
    this.candidate = new Candidate();
    this.resetImageInput();
  }

  addCandidate() {
    this.candidateService.add(this.candidate).subscribe(
      (response) => {
        console.log('Candidate created successfully:', response);
        this.candidates.push(response); // push response to update candidate
      },
      (error) => {
        console.error('Error creating candidate:', error);
      }
    );
  }

  confirmDelete(candidate: Candidate) {
    const message = this.translate.instant('confirm_delete_candidate', {
      firstName: candidate.firstName,
      lastName: candidate.lastName
    });
    if (confirm(message)) {
      this.deleteCandidate(candidate);
    }
  }

  deleteCandidate(candidate: Candidate): void {
    if (candidate.id !== null) {
      this.candidateService.isCandidateInAnyElection(candidate.id).subscribe(
        isInAnyElection => {
          if (isInAnyElection) {
            console.error('Candidate is in one or more elections!');
            this.errorMessage = this.translate.instant('candidate_in_election', {
              firstName: candidate.firstName,
              lastName: candidate.lastName
            });
          } else {
            if (candidate.id !== null) {
              this.candidateService.delete(candidate.id.toString()).subscribe(
                () => {
                  console.log('Candidate deleted successfully');
                  this.errorMessage = '';
                  this.candidates = this.candidates.filter(c => c.id !== candidate.id);
                  this.candidateService.getList().forEach(value => {
                    this.candidates = value;
                    this.tablePaginationService = new TablePagination(this.candidates, 10)
                  });
                },
                error => {
                  console.error('Error deleting candidate:', error);
                }
              );
            }
          }
        },
        error => {
          console.error('Error checking candidate in any election:', error);
        }
      );
    }
  }

  createCandidateButtonDisabled() {
    return this.candidate.schoolId.length <= 0 ||
      this.candidate.firstName.length <= 0 ||
      this.candidate.lastName.length <= 0 ||
      this.candidate.grade.length <= 0;
  }
}
