import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import {Voter} from "../entity/voter-model";
import {Candidate} from "../entity/candidate-model";
@Injectable({
  providedIn: 'root'
})
export class LeovoteWebApiService {
  headers = new HttpHeaders().set('Accept', 'application/json');
  baseUrl = 'http://localhost:8080/';
  candidatesAll = 'candidates';
  votersAll = 'voters'
  constructor(private http: HttpClient) { }
  public getAllVoters(){
    return this.http.get<Voter[]>(this.baseUrl + this.votersAll, {headers: this.headers});
  }
  public getAllCandidates(){
    return this.http.get<Candidate[]>(this.baseUrl + this.candidatesAll, {headers: this.headers});
  }
  constructor() { }
}
