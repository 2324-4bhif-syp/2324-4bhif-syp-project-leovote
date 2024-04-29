import {Component, OnInit} from '@angular/core';
import {Election} from "../shared/entity/election-model";
import {Result} from "../shared/entity/result";
import {ElectionService} from "../shared/control/election.service";
import {Chart, registerables} from "chart.js";

Chart.register(...registerables);


@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {
  elections: Election[] | undefined = undefined;
  selectedElection: Election | undefined = this.electionService.selectedElection;
  result: Result[] | undefined = undefined;
  resultError: boolean = false;
  public chart: any;

  constructor(public electionService: ElectionService) {
  }

  ngOnInit(): void {
    this.electionService.getList().forEach((value) => {
      this.elections = value;
    });
    this.getResult()
  }

  createChart(results: Result[]) {
    const labels = results.map((r) => `${r.firstname} ${r.lastname} (${r.grade})`);
    const data = results.map((r) => r.percentage);

    this.chart = new Chart("Results", {
      type: 'pie',
      data: {
        labels,
        datasets: [{
          label: 'Percent',
          data
        }],
      },
      options: {
        responsive:true,
        layout: {
          autoPadding:true
        },
        aspectRatio: 0.6,
        plugins: {
          legend: {
            position:"top",
            display:true,
          }
        }
      },
    });
  }

  getResult() {
    if (this.selectedElection !== undefined && this.selectedElection.id !== undefined && this.selectedElection.id !== null) {
      this.electionService.result(this.selectedElection.id.toString()).subscribe((value) => {
        const candidateResults: Result[] = [];
        if (value instanceof Object) {
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
              candidateResult.percentage = Math.round(candidateResult.percentage * 100) / 100;
              candidateResults.push(candidateResult);
            }
          });
        }
        console.log("GetResults candidates: ", candidateResults.forEach(value => value.lastname));
        this.result = candidateResults;
        console.log("GetResults this.result: ", this.result.forEach(value => value));
        this.createChart(candidateResults);
      }, (error) => {
        this.resultError = true;
      });
    }
  }
}
