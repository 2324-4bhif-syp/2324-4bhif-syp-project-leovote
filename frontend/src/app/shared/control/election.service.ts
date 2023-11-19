import { Injectable } from '@angular/core';
import {Election} from "../entity/election-model";
import {Candidate} from "../entity/candidate-model";
import {LeovoteWebApiService} from "../api/leovote-web-api.service";

@Injectable({
  providedIn: 'root'
})
export class ElectionService {
  protected elections: Election[] = [];
  constructor(private apiClient: LeovoteWebApiService) {
    this.apiClient.getAllElections()
      .subscribe({
        next: (elec) => {
          elec.forEach(e =>{
            e.electionStart = new Date((e.electionStart as unknown) as string)
            e.electionEnd = new Date((e.electionEnd as unknown) as string)
          });
          this.elections = elec;
        },
        error: (err) => {console.error(err)}
      });
  }
  add(election: Election){
    this.apiClient.addElection(election);
  }
}
