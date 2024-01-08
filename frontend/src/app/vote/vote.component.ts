import {Component} from '@angular/core';
import {VoteService} from "../shared/control/vote.service";
import {Vote} from "../shared/entity/vote";
import {Election} from "../shared/entity/election-model";
import {ElectionService} from "../shared/control/election.service";
import {Candidate} from "../shared/entity/candidate-model";

@Component({
  selector: 'app-vote',
  templateUrl: './vote.component.html',
  styleUrls: ['./vote.component.css']
})
export class VoteComponent {
  isVoted: boolean = false;
  voter: Vote | undefined = this.voteService.vote;
  election: Election | undefined = undefined;
  selecedCandidate: Candidate | undefined;

  voting() {
    if(this.selecedCandidate?.id != undefined && this.election?.id != undefined){
      this.voteService.voteCall(this.selecedCandidate.id, this.election.id);
      this.voteService.isLoggedIn = false;
      this.isVoted = true;
    }
  }

  constructor(
    public voteService: VoteService,
    public electionService: ElectionService,
  ) {
    electionService.getList().forEach(value => {
      value.forEach(election => {
        if (election.id === this.voter?.participatingIn) {
          this.election = election;
        }
      })
    })
  }
}
