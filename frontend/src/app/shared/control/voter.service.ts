import { Injectable } from '@angular/core';
import {Voter} from "../entity/voter-model";
import {LeovoteWebApiService} from "../api/leovote-web-api.service";

@Injectable({
  providedIn: 'root'
})
export class VoterService {
  protected voters: Voter[] = [];
  constructor(private apiClient: LeovoteWebApiService) {
    this.apiClient.getAllVoters()
      .subscribe((v) =>{
      this.voters = v;
    });
  }
}
