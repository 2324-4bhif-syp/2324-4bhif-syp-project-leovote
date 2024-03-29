import { Component } from '@angular/core';
import { Candidate } from "../shared/entity/candidate-model";
import { CandidateService } from "../shared/control/candidate.service";
import {throwError} from "rxjs";

@Component({
  selector: 'app-create-candidate',
  templateUrl: './create-candidate.component.html',
  styleUrls: ['./create-candidate.component.css']
})
export class CreateCandidateComponent {
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
    const header = rows.shift();
    for (const row of rows) {
      const [schoolId, firstName, lastName, grade] = row.split(';');
      if (schoolId && firstName && lastName && grade) {
        const candidate = new Candidate(schoolId, firstName, lastName, grade);
        candidate.pathOfImage = "default.jpg";
        this.candidate = candidate;
        this.createCandidate();
        console.log(this.candidate);
      } else {
        console.error('CSV data is not in the expected format for the row:', row);
      }
    }
  }


  createCandidate() {
    this.candidate.pathOfImage = "default.jpg";
    console.log(this.candidate)
    this.candidateService.add(this.candidate).subscribe(
      (response) => {
        console.log('Candidate created successfully:', response);
        this.candidates.push(response); // push response to update candidate
      },
      (error) => {
        console.error('Error creating candidate:', error);
      }
    );
    this.candidate = new Candidate();
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
