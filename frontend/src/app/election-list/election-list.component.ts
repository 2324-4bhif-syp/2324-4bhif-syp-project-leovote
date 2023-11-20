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
  constructor(private electionService: ElectionService) {}
  ngOnInit(): void {
    this.loadElections();
    this.electionService.onListRefresh().subscribe(() => {
      this.loadElections();
    });
  }
  create(createElection: Election) {
      this.doEdit = false;
      this.electionService.add(createElection).subscribe(
        response => {
          console.log('Election added successfully', response);
          this.loadElections();
        },
        error => {
          console.error('Error adding election', error);
        }
      );
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
