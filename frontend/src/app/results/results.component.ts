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

  constructor(
    public electionService: ElectionService,
  ) {
    electionService.getList().forEach(value => {
      this.elections = value;
    });
  }

  getResult() {
    if (this.selectedElection !== undefined && this.selectedElection.id !== undefined && this.selectedElection.id !== null) {
      this.electionService.result(this.selectedElection.id.toString()).forEach(value => {
        const candidateResults: Result[] = [];

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
        this.result = candidateResults;
      });
    }
  }

  addEmail() {
    if (this.selectedElection !== undefined &&
      this.selectedElection.id !== null) {
      this.electionService.addEmail(this.emailInput, this.selectedElection.id.toString()).forEach(value => {
        console.log(value);
        this.loadEmails();
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
