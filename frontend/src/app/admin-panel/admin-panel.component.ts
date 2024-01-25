import {Component} from '@angular/core';
import {ElectionService} from "../shared/control/election.service";
import {Election} from "../shared/entity/election-model";
import {Result} from "../shared/entity/result";

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent {
  elections: Election[] | undefined = undefined;
  selectedElection: Election | undefined = undefined;
  result: Result[] | undefined = undefined;

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
      }).then(r =>{
        console.log("Blockchain has been modified");
      });
    }
  }
}
