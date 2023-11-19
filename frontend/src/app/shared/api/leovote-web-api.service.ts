import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import {Voter} from "../entity/voter-model";
import {Candidate} from "../entity/candidate-model";
import {Election} from "../entity/election-model";
@Injectable({
  providedIn: 'root'
})
export class LeovoteWebApiService {
  headers = new HttpHeaders().set('Accept', 'application/json');
  baseUrl = 'http://localhost:8080/';
  candidates = 'candidates';
  voters = 'voters';
  elections: string = 'elections';
  constructor(private http: HttpClient) { }
  public getAllVoters(){
    return this.http.get<Voter[]>(this.baseUrl + this.voters, {headers: this.headers});
  }
  public getAllCandidates(){
    return this.http.get<Candidate[]>(this.baseUrl + this.candidates, {headers: this.headers});
  }
  public getAllElections(){
    return this.http.get<Election[]>(this.baseUrl + this.elections, {headers: this.headers});
  }
  public addCandidate(candidate: Candidate){
    return this.http.post<number>(this.baseUrl + this.candidates, candidate, {headers: this.headers});
  }
  public addElection(election: Election){
    return this.http.post(this.baseUrl + this.candidates, election, {headers: this.headers});
  }
}
