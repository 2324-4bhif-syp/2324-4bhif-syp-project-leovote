import { Component } from '@angular/core';
import {Election} from "../shared/entity/election-model";
import {Result} from "../shared/entity/result";
import {EmailModel} from "../shared/entity/email-model";
import {ElectionService} from "../shared/control/election.service";

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent {
  elections: Election[] | undefined = undefined;
  selectedElection: Election | undefined = undefined;
  result: Result[] | undefined = undefined;
  emailInput: string = "";
  emails: EmailModel[] | undefined;
  emailError: boolean = false;
  resultError: boolean = false;

  constructor(
    public electionService: ElectionService,
  ) {
    electionService.getList().forEach(value => {
      this.elections = value;
    });
  }

  getResult() {
    if (this.selectedElection !== undefined && this.selectedElection.id !== undefined && this.selectedElection.id !== null) {
      this.electionService.result(this.selectedElection.id.toString()).subscribe((value) => {
        const candidateResults: Result[] = [];
        if(value instanceof Object){
          this.resultError = false;
          Object.keys(value).forEach(key => {
            console.log(key);
            console.log(value);
            const match = key.match(/Firstname: (\w+) Lastname: (\w+) Grade: (\w+)/);
            if (match) {
              let percentage: number = (value as { [key: string]: number })[key];
              const [, firstname, lastname, grade] = match;
              const candidateResult: Result = {
                firstname,
                lastname,
                grade,
                percentage
              };
              candidateResults.push(candidateResult);
            }
          });
        }
        this.result = candidateResults;
      }, (error) => {
        this.resultError = true;
      });
    }
  }

  addEmail() {
    if (this.selectedElection !== undefined &&
      this.selectedElection.id !== null) {
      this.electionService.addEmail(this.emailInput, this.selectedElection.id.toString()).subscribe((value) => {
          console.log(value);
          this.loadEmails();
          this.emailError = false;
        },
        (error) => {
          this.emailError = true;
        })
    }
  }

  loadEmails() {
    if (this.selectedElection !== undefined &&
      this.selectedElection.id !== null) {
      this.electionService.getMails(this.selectedElection.id.toString()).forEach(value => {
        this.emails = value;
      })
    }
  }

  deleteEmail(id: number) {
    if(Number(id)){
      this.electionService.removeMail(id.toString()).forEach(value => {
        this.loadEmails();
      });
    }
  }

  sendCodes() {

  }
}
