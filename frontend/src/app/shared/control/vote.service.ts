import { Injectable } from '@angular/core';
import {LeovoteWebApiService} from "../api/leovote-web-api.service";
import {Vote} from "../entity/vote";
@Injectable({
  providedIn: 'root'
})
export class VoteService {
  protected vote: Vote | undefined;
  isLoggedIn: boolean = false;
  constructor(private apiClient: LeovoteWebApiService) {
  }
  checkCode(code: string): boolean {
    console.log(code);

    this.apiClient.getVoteByCode(code).subscribe(
      (v: Vote) => {
        this.vote = v;
        //this.vote.voted = false; da alle Votes automatisch auf true gesetzt wird
        if (this.vote !== undefined && !this.vote.voted) {
          this.isLoggedIn = true;
        }
      },
      (error) => {
        console.error('Error fetching vote:', error);
      }
    );

    return this.isLoggedIn;
  }
}
