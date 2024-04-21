import {Component} from '@angular/core';
import {ElectionService} from "../shared/control/election.service";
import {Election} from "../shared/entity/election-model";
import {AdminService} from "../shared/control/admin.service";

@Component({
  selector: 'app-admin-election-list',
  templateUrl: './admin-election-list.component.html',
  styleUrls: ['./admin-election-list.component.css']
})
export class AdminElectionListComponent {
  elections: Election[] | undefined = undefined;

  constructor(public electionService: ElectionService) {
    this.loadElections()
  }

  selectElection(election: Election) {
    this.electionService.selectedElection = election;
  }

  loadElections(){
    this.electionService.getList().forEach(value => {
      this.elections = value;
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

  deleteElection(id: number | null) {
    if(id != null){
      this.electionService.delete(id.toString()).subscribe(() => {
        this.loadElections()
      });
    } else {
      console.log("Election could not be deleted")
    }
  }

    protected readonly Date = Date;
}
