import {Component} from '@angular/core';
import {Election} from "../shared/entity/election-model";
import {Result} from "../shared/entity/result";
import {EmailModel} from "../shared/entity/email-model";
import {ElectionService} from "../shared/control/election.service";

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
  }

  ngOnInit(): void {
    google.charts.load("current", {packages: ["corechart"]});
    google.charts.setOnLoadCallback(this.drawChart);
    console.log("DRAW CHART");
  }

  drawChart() {
    var dataArray = [['Language', 'Speakers (in millions)'],
      ['German',  5.85],
      ['French',  1.66],
      ['Italian', 0.316],
      ['Romansh', 0.0791]];
    if(this.result !== undefined) {
      for (var i = 0; i < this.result.length; i++) {
        // Extract lastname and percent values from the current object
        var lastname = this.result[i].lastname;
        var percent = this.result[i].percentage;

        // Push an array containing lastname and percent into the data array
        dataArray.push([lastname, percent]);
      }
    }
    var data = google.visualization.arrayToDataTable(dataArray);

    var chart = new google.visualization.PieChart(document.getElementById('donutchart'));
    chart.draw(data, null)
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
