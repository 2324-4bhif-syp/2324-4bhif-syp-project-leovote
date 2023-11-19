import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Candidate} from "../entity/candidate-model";
import {Election} from "../entity/election-model";
import {Vote} from "../entity/vote-model";
@Injectable({
  providedIn: 'root'
})
export class LeovoteWebApiService {
  headers = new HttpHeaders().set('Accept', 'application/json');
  baseUrl = 'http://localhost:8080/';
  candidates = 'candidates';
  votes= 'votes';
  elections: string = 'elections';
  constructor(private http: HttpClient) { }
  public getAllCandidates(){
    return this.http.get<Candidate[]>(this.baseUrl + this.candidates, {headers: this.headers});
  }
  public getAllElections(){
    return this.http.get<Election[]>(this.baseUrl + this.elections, {headers: this.headers});
  }
  public getAllVotes(){
    return this.http.get<Vote[]>(this.baseUrl + this.votes, {headers: this.headers});
  }
  public addCandidate(candidate: Candidate){
    return this.http.post(this.baseUrl + this.candidates, candidate, {headers: this.headers});
  }
  public addElection(election: Election){
    console.log("Id: " + election.id + ", Name: " + election.name + ", Date1: " + election.electionStart.toLocaleDateString() + ", Date2: " + election.electionEnd.toLocaleDateString() + ", Type: " + election.electionType);
    console.log(this.baseUrl + this.elections, election)
    return this.http.post(this.baseUrl + this.elections, election, {headers: this.headers});
  }
  public addVote(vote: Vote){
    return this.http.post(this.baseUrl + this.votes, vote, {headers: this.headers});
  }
}
