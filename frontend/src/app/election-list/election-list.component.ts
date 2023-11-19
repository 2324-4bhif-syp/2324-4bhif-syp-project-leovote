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
    this.doEdit = false;
    this.electionService.add(this.createElection).subscribe(
      response => {
        console.log('Election added successfully', response);
        this.electionService.refreshList(); // Trigger a list refresh after adding an election
      },
      error => {
        console.error('Error adding election', error);
      }
    );
    this.createElection = this.initElection();
  }

  parseDate(eventData: Event){
    const dateString = (eventData.target as HTMLInputElement).value;
    if(dateString){
      return new Date(dateString);
    }
    return new Date();
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
