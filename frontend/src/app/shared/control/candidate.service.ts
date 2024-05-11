import { Injectable } from '@angular/core';
import { Candidate } from "../entity/candidate-model";
import { LeovoteWebApiService } from "../api/leovote-web-api.service";
import { map, Observable, Subject } from "rxjs";
import {CandidateImage} from "../entity/candidate-image";

@Injectable({
  providedIn: 'root'
})
export class CandidateService {
  private refreshListSubject = new Subject<void>();

  constructor(private apiClient: LeovoteWebApiService) {
  }

  add(candidate: Candidate): Observable<any> {
    return this.apiClient.addCandidate(candidate);
  }

  delete(candidateId: string) {
    return this.apiClient.deleteCandidate(candidateId);
  }
  getList(): Observable<Candidate[]> {
    return this.apiClient.getAllCandidates().pipe(
      map(candidates => {
        return candidates.map(candidate => {
          return candidate;
        });
      })
    );
  }
  isCandidateInAnyElection(candidateId: number): Observable<boolean> {
    return this.apiClient.getAllElections().pipe(
      map(elections => {
        return elections.some(election =>
          election.participatingCandidates.some(candidate => candidate.id === candidateId));
      })
    );
  }
  refreshList(): void {
    this.refreshListSubject.next();
  }

  onListRefresh(): Observable<void> {
    return this.refreshListSubject.asObservable();
  }

  uploadImage(image: File) {
    return this.apiClient.uploadImage(image);
  }

  candidateImage() {
    return this.apiClient.getImagesAndCandidates();
  }

  getById(id: number): Observable<Candidate> {
    return this.apiClient.getCandidateById(id);
  }

  getImageById(id: number): Observable<CandidateImage> {
    return this.apiClient.getImageById(id);
  }
}
