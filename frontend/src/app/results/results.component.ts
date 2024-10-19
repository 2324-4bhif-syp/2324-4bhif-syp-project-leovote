import {Component, OnInit} from '@angular/core';
import {Election} from "../shared/entity/election-model";
import {Result} from "../shared/entity/result";
import {ElectionService} from "../shared/control/election.service";
import {Chart, registerables} from "chart.js";
import {CandidateService} from "../shared/control/candidate.service";
import {forkJoin, map} from "rxjs";

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

  constructor(public electionService: ElectionService,
              public candidateService: CandidateService) {
  }

  ngOnInit(): void {
    this.electionService.getList().forEach((value) => {
      this.elections = value;
    });
    this.getResult()
  }

  getResult() {
    if (this.selectedElection !== undefined && this.selectedElection.id !== undefined && this.selectedElection.id !== null) {
      this.electionService.result(this.selectedElection.id.toString()).subscribe((value) => {
        const candidateResults: Result[] = [];
        if (value instanceof Object) {
          this.resultError = false;

          const observables = Object.keys(value).map(key => {
            return this.candidateService.getBySchoolId(key).pipe(
              map(candidate => {
                if (candidate) {
                  let percentage: number = (value as { [key: string]: number })[key];
                  const candidateResult: Result = new Result(candidate.firstName,
                    candidate.lastName, candidate.grade, percentage);

                  candidateResult.percentage = Math.round(candidateResult.percentage * 100) / 100;
                  candidateResults.push(candidateResult);
                }
              })
            );
          });
          forkJoin(observables).subscribe(() => {
            this.createChart(candidateResults);
            this.result = candidateResults;
          });
        }
      }, (error) => {
        this.resultError = true;
      });
    }
  }


  createChart(results: Result[]) {
    let labels: string[] = [];
    for (let i = 0; i < results.length; i++) {
      labels.push(`${results[i].firstname} ${results[i].lastname} (${results[i].grade}): ${results[i].percentage}%`);
    }
    const data = results.map((r) => r.percentage);

    this.chart = new Chart("Results", {
      type: 'pie',
      data: {
        labels: labels,
        datasets: [{
          label: 'Percent',
          borderWidth: 1,
          data: data
        }],
      },
      options: {
        responsive:true,
        layout: {
          autoPadding:true
        },
        aspectRatio: 1,
        plugins: {
          legend: {
            position:"top",
            display:true,
            align:"start",
          }
        }
      },
    });
  }
}
