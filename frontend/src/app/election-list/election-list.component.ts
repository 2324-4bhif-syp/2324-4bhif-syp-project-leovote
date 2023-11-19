import { Component, Input, OnInit } from '@angular/core';
import { Election } from '../shared/entity/election-model';
import { ElectionService } from '../shared/control/election.service';

@Component({
  selector: 'app-election-list',
  templateUrl: './election-list.component.html',
  styleUrls: ['./election-list.component.css']
})
export class ElectionListComponent implements OnInit {
  @Input() doEdit: boolean = false;
  elections: Election[] = [];
  createElection: Election = this.initElection();
  isError: boolean = false;
  constructor(private electionService: ElectionService) {}

  ngOnInit(): void {
    this.loadElections();
    this.electionService.onListRefresh().subscribe(() => {
      this.loadElections();
    });
  }

  initElection(): Election {
    return new Election(null, '', new Date(), new Date(), '');
  }

  create() {
    if (this.isEndDateValid(this.createElection.electionStart, this.createElection.electionEnd)) {
      this.doEdit = false;
      this.isError = false;
      this.electionService.add(this.createElection).subscribe(
        response => {
          console.log('Election added successfully', response);
          this.loadElections();
        },
        error => {
          console.error('Error adding election', error);
        }
      );
      this.createElection = this.initElection();
    } else {
      console.error('End date must be equal to or after start date.');
      this.isError = true;
    }
  }
  parseDate(eventData: Event){
    const dateString = (eventData.target as HTMLInputElement).value;
    if(dateString){
      return new Date(dateString);
    }
    return new Date();
  }
  isEndDateValid(startDate: Date, endDate: Date): boolean {
    const startDay = new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate());
    const endDay = new Date(endDate.getFullYear(), endDate.getMonth(), endDate.getDate());
    return !endDate || !startDate || endDay >= startDay;
  }
  private loadElections() {
    this.electionService.getList().subscribe(
      elections => {
        console.log(elections);
        this.elections = elections;
      },
      error => {
        console.error(error);
      }
    );
  }
}
