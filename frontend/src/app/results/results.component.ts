import {Component} from '@angular/core';
import {Election} from "../shared/entity/election-model";
import {Result} from "../shared/entity/result";
import {EmailModel} from "../shared/entity/email-model";
import {ElectionService} from "../shared/control/election.service";
import {Chart, registerables} from "chart.js";

Chart.register(...registerables);

declare var google: any;

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent {
  elections: Election[] | undefined = undefined;
  selectedElection: Election | undefined = this.electionService.selectedElection;
  result: Result[] | undefined = undefined;
  emailInput: string = "";
  emails: EmailModel[] | undefined;
  emailError: boolean = false;
  resultError: boolean = false;
  isCsvUploaded: boolean = false;
  csvData: string = "";
  myChart: any;

  constructor(
    public electionService: ElectionService,
  ) {
    electionService.getList().forEach(value => {
      this.elections = value;
    });
    this.getResult();
    console.log("Result in Constructor ", this.result?.length);
  }


  ngOnInit(): void {
    this.RenderChart();
  }

  RenderChart() {
    let candidates: string[] = [];
    this.getResult();
    if (this.result != undefined) {
      console.log("Result in RenderChart ", this.result?.length);
      for (let i = 0; i < this.result.length; i++) {
        candidates.push(this.result[i].lastname);
      }
    }
    new Chart("piechart", {
      type: 'bar',
      data: {
        labels: candidates,
        datasets: [{
          label: 'Number of Votes',
          data: [12, 19, 3, 5, 2, 3],
          borderWidth: 2
        }]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.onload = () => {
      const fileType = file.name.split('.').pop()?.toLowerCase();
      if (fileType !== 'csv') {
        console.error('Not an CSV type');
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
    // Entferne Leerzeichen und leere Zeilen
    const data = rows.map(row => row.trim()).filter(row => row);
    console.log(data)
    if (this.selectedElection !== undefined &&
      this.selectedElection.id !== null) {
      this.electionService.addMultipleEmails(data, this.selectedElection.id.toString()).subscribe((value) => {
          console.log(value);
          this.loadEmails();
          this.emailError = false;
        },
        (error) => {
          this.emailError = true;
        })
    }
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
    if (Number(id)) {
      this.electionService.removeMail(id.toString()).forEach(value => {
        this.loadEmails();
      });
    }
  }

  sendCodes() {
    if (this.selectedElection !== undefined &&
      this.selectedElection.id !== null) {
      this.electionService.sendCodes(this.selectedElection.id.toString()).subscribe((value) => {
        console.log("emails sent");
      }, (error) => {
        console.log("error while sending mail");
      });
    }
  }
}
