import {Component, OnInit} from '@angular/core';
import {ElectionService} from "../shared/control/election.service";
import {Election} from "../shared/entity/election-model";
import {TranslateService} from '@ngx-translate/core';
import {TablePagination} from "../shared/service/TablePaginationService";

@Component({
  selector: 'app-admin-election-list',
  templateUrl: './admin-election-list.component.html',
  styleUrls: ['./admin-election-list.component.css']
})
export class AdminElectionListComponent {
  elections: Election[] | undefined = undefined;

  // sample data for pagination:

  sampleCandidates = [
    {name: "Adam", class: "1CHIF"},
    {name: "Something", class: "1BHIF"},
    {name: "Alan", class: "3CHIF"},
    {name: "Echo", class: "4BHIF"},
    {name: "Charlie", class: "2CHIF"},
    // Add more rows here as needed
  ];

  tablePaginationService = new TablePagination(this.sampleCandidates, 3)


  constructor(public electionService: ElectionService, private translate: TranslateService) {
    this.loadElections()
  }

  selectElection(election: Election) {
    this.electionService.selectedElection = election;
  }

  loadElections() {
    this.electionService.getList().forEach(value => {
      this.elections = value;
      this.tablePaginationService = new TablePagination(this.elections, 9)
    });
  }

  isDateBeforeNow(date: Date): boolean {
    const now = new Date();
    return date < now;
  }

  isDateAfterNow(date: Date): boolean {
    const now = new Date();
    return date > now;
  }

  confirmDelete(id: number | null, election: Election) {
    if (confirm(this.translate.instant('confirm_delete', {name: election.name}))) {
      this.deleteElection(id);
    }
  }

  deleteElection(id: number | null) {
    if (id != null) {
      this.electionService.delete(id.toString()).subscribe(() => {
        this.loadElections()
      });
    } else {
      console.log("Election could not be deleted")
    }
  }

  protected readonly Date = Date;
}
