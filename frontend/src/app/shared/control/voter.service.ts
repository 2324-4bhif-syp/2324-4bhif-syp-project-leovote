import { Injectable } from '@angular/core';
import {Voter} from "../entity/voter-model";
import {LeovoteWebApiService} from "../api/leovote-web-api.service";

@Injectable({
  providedIn: 'root'
})
export class VoterService {
  constructor(private apiClient: LeovoteWebApiService) {
    this.apiClient.getAllVoters()
      .subscribe((v) =>{
      this.voters = v;
    });
  }
  protected voters: Voter[] = [];
  createVoter(voter: Voter){
    this.voters.push(voter);
  }

  deleteVoter(index: number){
    this.voters.splice(index, 1);
  }
  updateVoter(voter: Voter, index: number){
    this.voters[index] = voter;
  }
}
