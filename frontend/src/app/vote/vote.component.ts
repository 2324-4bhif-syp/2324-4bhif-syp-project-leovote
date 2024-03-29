import {Component} from '@angular/core';
import {VoteService} from "../shared/control/vote.service";
import {Vote} from "../shared/entity/vote";
import {Election} from "../shared/entity/election-model";
import {ElectionService} from "../shared/control/election.service";
import {Candidate} from "../shared/entity/candidate-model";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-vote',
  templateUrl: './vote.component.html',
  styleUrls: ['./vote.component.css']
})
export class VoteComponent {
  isVoted: boolean = false;
  isSelected: boolean = false;

  voter: Vote | undefined = this.voteService.vote;
  election: Election | undefined = undefined;
  selectedCandidate: Candidate | undefined;

  voting() {
    if (this.selectedCandidate?.id != undefined && this.election?.id != undefined) {
      this.voteService.voteCall(this.selectedCandidate.id, this.election.id);
      this.voteService.isLoggedIn = false;
      this.isVoted = true;
    }
  }

  select(){
    this.isSelected = true;
  }

  constructor(
    public voteService: VoteService,
    public electionService: ElectionService,
    public keycloakService: KeycloakService
  ) {
    electionService.getList().forEach(value => {
      value.forEach(election => {
        if (election.id === this.voter?.participatingIn) {
          this.election = election;
        }
      })
    })
  }

  deselect() {
    this.isSelected = false;
  }
}
