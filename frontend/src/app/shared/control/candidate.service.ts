import { Injectable } from '@angular/core';
import {Candidate} from "../entity/candidate-model";

@Injectable({
  providedIn: 'root'
})
export class CandidateService {
  private url: string = 'http://localhost:8080/candidates';
  private candidates: Candidate[] =[];
  constructor() { }
  public async getCandidates(): Promise<Candidate[]> {
    let response = await fetch(this.url);
    let data = await response.json();
    this.candidates = data.map((candidate: any) => {
      let candidate1: Candidate = {
        schoolId: candidate.schoolId,
        firstName: candidate.firstName,
        lastName: candidate.lastName,
        grade: candidate.grade
      }
      return candidate1;
    })
    return this.candidates;
  }
  public async createCandidate(candidate: Candidate) {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    try {
      let response = await fetch(this.url, {
        body: JSON.stringify(candidate),
        headers: headers,
        method: "POST"
      });

      if(response.status !== 201) {
        console.error("Error, could not add candidate");
      }
    } catch (error) {
      console.error("Error, could not add candidate");
    }
    this.candidates.push(candidate);
  }
}
