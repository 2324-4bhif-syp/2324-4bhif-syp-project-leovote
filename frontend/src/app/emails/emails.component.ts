import {Component} from '@angular/core';
import {Election} from "../shared/entity/election-model";
import {EmailModel} from "../shared/entity/email-model";
import {ElectionService} from "../shared/control/election.service";
import {TablePagination} from "../shared/service/TablePaginationService";

@Component({
  selector: 'app-emails',
  templateUrl: './emails.component.html',
  styleUrls: ['./emails.component.css']
})
export class EmailsComponent {
  elections: Election[] | undefined = undefined;
  selectedElection: Election | undefined = this.electionService.selectedElection;
  emailInput: string = "";
  emails: EmailModel[] | undefined;
  emailCount: number = 0;
  emailError: boolean = false;
  isCsvUploaded: boolean = false;
  csvData: string = "";
  sendingError: boolean = false;
  sent: boolean = false;
  filename: string = "";
  tablePaginationService = new TablePagination([], 3)

  constructor(
    public electionService: ElectionService,
  ) {
    electionService.getList().forEach(value => {
      this.elections = value;
    });
    this.loadEmails()
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    this.filename = file.name;
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
        this.emailCount = value.length;
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
        this.sendingError = false;
        this.sent = true;
      }, (error) => {
        console.log("error while sending mail");
        this.sendingError = true;
        this.sent = false;
      });
    }
  }
}
