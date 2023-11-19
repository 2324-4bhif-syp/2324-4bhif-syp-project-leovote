import { Injectable } from '@angular/core';
import {Candidate} from "../entity/candidate-model";
import {LeovoteWebApiService} from "../api/leovote-web-api.service";
import {map, Observable, Subject} from "rxjs";
import {Election} from "../entity/election-model";

@Injectable({
  providedIn: 'root'
})
export class CandidateService {
  private refreshListSubject = new Subject<void>();
  constructor(private apiClient: LeovoteWebApiService) {}
  add(candidate: Candidate): Observable<any> {
    return this.apiClient.addCandidate(candidate);
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
  refreshList(): void {
    this.refreshListSubject.next();
  }

  onListRefresh(): Observable<void> {
    return this.refreshListSubject.asObservable();
  }
}
