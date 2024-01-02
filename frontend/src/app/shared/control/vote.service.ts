import { Injectable } from '@angular/core';
import { LeovoteWebApiService } from "../api/leovote-web-api.service";
import { Vote } from "../entity/vote";
import {VoteCandidate} from "../entity/vote-candidate-model";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class VoteService {
  public vote: Vote | undefined;
  isLoggedIn: boolean = false;

  constructor(private apiClient: LeovoteWebApiService,
              private router: Router) {}

  checkCode(code: string): Promise<boolean> {
    console.log(code);

    return new Promise<boolean>((resolve, reject) => {
      this.apiClient.getVoteByCode(code).subscribe(
        (v: Vote) => {
          this.vote = v;
          if (this.vote !== undefined && !this.vote.voted) {
            this.isLoggedIn = true;
            resolve(true);
          } else {
            resolve(false);
          }
        },
        (error) => {
          console.error('Error fetching vote:', error);
          reject(error);
        }
      );
    });
  }

  voteCall(candidateId: number, electionId: number){
    if(this.vote?.generatedId != undefined){
      let voteCandidate: VoteCandidate = new VoteCandidate(this.vote.generatedId)
      console.log(voteCandidate);
      this.apiClient.voteForCandidate(voteCandidate, candidateId, electionId).subscribe(
        () => {
          console.log('Vote successful');
          this.isLoggedIn = false;
          this.router.navigate(['/login']);
        },
        (error) => {
          console.error('Error in voting');
        }
      );
    } else {
      console.log("No Id given");
    }
  }
}
