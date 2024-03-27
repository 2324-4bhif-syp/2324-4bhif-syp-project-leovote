import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Candidate} from "../entity/candidate-model";
import {Election} from "../entity/election-model";
import {Vote} from "../entity/vote";
import {VoteCandidate} from "../entity/vote-candidate-model";
import {EmailModel} from "../entity/email-model";
import {LoginModel} from "../entity/login-model";
@Injectable({
  providedIn: 'root'
})
export class LeovoteWebApiService {

  headers = new HttpHeaders().set('Accept', 'application/json');
  //baseUrl = 'http://89.168.107.125/api/';   it's here for the server. This is used at server
  private baseUrl = 'http://localhost:8080/'; // should be changed afterwards
  private candidates = 'candidates';
  private elections: string = 'elections';
  private addElectionUrl: string = 'elections/election';
  private electionById: string = 'elections/${id}'
  private voters: string = 'voters/voter/${id}';
  private vote: string = 'voters/vote/${electionId}/${candidateId}';
  private electionResult: string = 'elections/results/${id}';
  private addEmailUrl: string = 'elections/addEmail/${id}/${email}';
  private addMultipleEmailsUrl: string = 'elections/addEmail/multiples/${electionId}';
  private allMails: string = 'email/${electionId}';
  private removeMailUrl: string = 'elections/removeEmail/${id}';
  private sendCodesUrl: string = 'email/election/${electionId}';
  private removeCandidate: string = 'candidates/${id}';
  private removeElection: string = 'elections/${id}';
  private checkLoginDataUrl: string = 'token';
  private checkEmailAndCodeUrl: string = 'voters/voter/${email}/${code}';

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
  public deleteCandidate(candidateId: string) {
    return this.http.delete(this.baseUrl + this.removeCandidate.replace('${id}', candidateId), {headers: this.headers});
  }
  public addElection(election: Election){
    return this.http.post(this.baseUrl + this.addElectionUrl, election, {headers: this.headers});
  }
  public deleteElection(electionId: string) {
    return this.http.delete(this.baseUrl + this.removeElection.replace('${id}', electionId), {headers: this.headers});
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
    return this.http.get<EmailModel[]>(this.baseUrl + this.allMails.replace('${electionId}', electionId), {headers: this.headers});
  }

  public removeMail(mailId: string){
    return this.http.delete(this.baseUrl + this.removeMailUrl.replace('${id}', mailId), {headers: this.headers});
  }

  public sendCodes(electionId: string){
    return this.http.post(this.baseUrl + this.sendCodesUrl.replace('${electionId}', electionId), {headers: this.headers});
  }
  public addMultipleEmails(email: string[], electionId: string) {
    return this.http.post(this.baseUrl + this.addMultipleEmailsUrl.replace('${electionId}', electionId), email, {headers: this.headers});
  }

  public checkLoginData(schoolId: string, password: string){
    const body = {
      username: schoolId,
      password: password
    };

    return this.http.post<LoginModel>(this.baseUrl + this.checkLoginDataUrl, body, {headers: this.headers});
  }

  public checkEmailAndCode(email: string, code: string) {
    return this.http.get<boolean>(this.baseUrl + this.checkEmailAndCodeUrl
      .replace('${email}', email)
      .replace('${code}', code), {headers: this.headers});
  }
}
