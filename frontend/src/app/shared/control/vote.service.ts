import { Injectable } from '@angular/core';
import { LeovoteWebApiService } from "../api/leovote-web-api.service";
import { Vote } from "../entity/vote";

@Injectable({
  providedIn: 'root'
})
export class VoteService {
  public vote: Vote | undefined;
  isLoggedIn: boolean = false;

  constructor(private apiClient: LeovoteWebApiService) {}

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
}
