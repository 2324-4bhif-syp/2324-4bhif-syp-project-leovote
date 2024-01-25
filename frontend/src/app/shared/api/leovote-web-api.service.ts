import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Candidate} from "../entity/candidate-model";
import {Election} from "../entity/election-model";
import {Vote} from "../entity/vote";
import {VoteCandidate} from "../entity/vote-candidate-model";
@Injectable({
  providedIn: 'root'
})
export class LeovoteWebApiService {

  headers = new HttpHeaders().set('Accept', 'application/json');
  //baseUrl = 'http://89.168.107.125/api/';   it's here for the server. This is used at server
  baseUrl = 'http://localhost:8080/';
  candidates = 'candidates';
  elections: string = 'elections';
  electionById: string = 'elections/${id}'
  voters: string = 'voters/voter/${id}';
  vote: string = 'voters/vote/${electionId}/${candidateId}';
  electionResult: string = 'elections/results/${id}';
  addEmailUrl: string = 'elections/addEmail/${id}/${email}';
  allMails: string = 'elections/emails/${electionId}'
  constructor(private http: HttpClient) { }
  public getAllCandidates(){
    return this.http.get<Candidate[]>(this.baseUrl + this.candidates, {headers: this.headers});
  }
  public getAllElections(){
    return this.http.get<Election[]>(this.baseUrl + this.elections, {headers: this.headers});
  }
  public voteForCandidate(voter: VoteCandidate, candidateId: number, electionId: number){
    return this.http.post(this.baseUrl + this.vote.replace('${electionId}', electionId.toString())
      .replace('${candidateId}', candidateId.toString()), voter, {headers: this.headers});
  }
  public addCandidate(candidate: Candidate){
    return this.http.post(this.baseUrl + this.candidates, candidate, {headers: this.headers});
  }
  public addElection(election: Election){
    return this.http.post(this.baseUrl + this.elections, election, {headers: this.headers});
  }
  public getVoteByCode(code: string) {
    return this.http.get<Vote>(this.baseUrl + this.voters.replace('${id}', code),
      {headers: this.headers});
  }
  public getResultByElection(electionId: string){
    return this.http.get<Object>(this.baseUrl + this.electionResult.replace('${id}', electionId), {headers: this.headers});
  }

  public addEmail(email: string, electionId: string){
    return this.http.post(this.baseUrl + this.addEmailUrl.replace('${id}', electionId).replace('${email}', email), {headers: this.headers});
  }

  public getAllMails(electionId: string){
    return this.http.get<string[]>(this.baseUrl + this.allMails.replace('${electionId}', electionId), {headers: this.headers});
  }
}
