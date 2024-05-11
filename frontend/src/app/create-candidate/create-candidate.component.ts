import {Component} from '@angular/core';
import {Candidate} from "../shared/entity/candidate-model";
import {CandidateService} from "../shared/control/candidate.service";
import {throwError} from "rxjs";

@Component({
  selector: 'app-create-candidate',
  templateUrl: './create-candidate.component.html',
  styleUrls: ['./create-candidate.component.css']
})
export class CreateCandidateComponent {
  imageFile: File | null = null;
  candidate: Candidate = new Candidate();
  isCsvUploaded: boolean = false;
  csvData: string = "";
  candidates: Candidate[] = [];
  errorMessage: string = '';

  constructor(protected candidateService: CandidateService) {
    candidateService.getList().forEach(value => {
      this.candidates = value;
    });
  }

  onImageChange(event: any) {
    this.imageFile = event.target.files[0];
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    const reader = new FileReader();
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
      const [schoolId, firstName, lastName, grade, pathOfImage] = row.split(',');
      if (schoolId && firstName && lastName && grade) {
        console.log(pathOfImage)
        const candidate = new Candidate(schoolId, firstName, lastName, grade, pathOfImage);
        console.log("KAndidaten: ", candidate)
        if (!pathOfImage) {
          console.log("WARUMMMM")
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
    console.log(this.candidate)
    if (this.imageFile) {
      this.candidateService.uploadImage(this.imageFile,this.candidate.id).subscribe(
        (response: string) => {
          console.log('Bild erfolgreich hochgeladen:', response);
        },
        (error) => {
          console.error('Fehler beim Hochladen des Bildes:', error);
        }
      );
      if (this.imageFile != null) {
        this.candidate.pathOfImage = this.imageFile.name;
        console.log(this.imageFile.name)
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
    if (confirm("Are you sure you want to delete this Candidate?")) {
      this.deleteCandidate(candidate);
    }
  }

  deleteCandidate(candidate: Candidate): void {
    if (candidate.id !== null) {
      this.candidateService.isCandidateInAnyElection(candidate.id).subscribe(
        isInAnyElection => {
          if (isInAnyElection) {
            console.error('Candidate is in one or more elections!');
            this.errorMessage = `Candidate ${candidate.firstName} ${candidate.lastName} is in one or more elections!`
          } else {
            if (candidate.id !== null) {
              this.candidateService.delete(candidate.id.toString()).subscribe(
                () => {
                  console.log('Candidate deleted successfully');
                  this.errorMessage = '';
                  this.candidates = this.candidates.filter(c => c.id !== candidate.id);
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
}
