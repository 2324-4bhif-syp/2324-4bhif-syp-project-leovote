import {Component} from '@angular/core';
import {VoteService} from "../shared/control/vote.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Vote} from "../shared/entity/vote";
import {Election} from "../shared/entity/election-model";

@Component({
  selector: 'app-vote',
  templateUrl: './vote.component.html',
  styleUrls: ['./vote.component.css']
})
export class VoteComponent {
  voter: Vote | undefined = this.voteService.vote;
  election: Election | undefined = undefined;


  constructor(
    public voteService: VoteService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }
}
