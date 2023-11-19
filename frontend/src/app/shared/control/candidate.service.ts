import { Injectable } from '@angular/core';
import {Candidate} from "../entity/candidate-model";
import {LeovoteWebApiService} from "../api/leovote-web-api.service";

@Injectable({
  providedIn: 'root'
})
export class CandidateService {
  protected candidates: Candidate[]
  constructor(private apiClient: LeovoteWebApiService) {
    this.apiClient.getAllCandidates()
      .subscribe((c) => {
        this.candidates = c;
      });
  }
  add(candidate: Candidate){
    this.apiClient.addCandidate(candidate);
  }
}
