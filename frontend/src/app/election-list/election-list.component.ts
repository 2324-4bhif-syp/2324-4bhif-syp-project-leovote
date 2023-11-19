import { Component } from '@angular/core';
import {Election} from "../shared/entity/election-model";
import {ElectionService} from "../shared/control/election.service";

@Component({
  selector: 'app-election-list',
  templateUrl: './election-list.component.html',
  styleUrls: ['./election-list.component.css']
})
export class ElectionListComponent {
  elections: Election[] = [];
  constructor(private electionService: ElectionService) {
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
