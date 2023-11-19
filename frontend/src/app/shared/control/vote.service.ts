import { Injectable } from '@angular/core';
import {LeovoteWebApiService} from "../api/leovote-web-api.service";
import {map, Observable, Subject} from "rxjs";
import {Election} from "../entity/election-model";
import {Vote} from "../entity/vote-model";

@Injectable({
  providedIn: 'root'
})
export class VoteService {
  constructor(private apiClient: LeovoteWebApiService) {}
  private refreshListSubject = new Subject<void>();
  add(vote: Vote): Observable<any> {
    return this.apiClient.addVote(vote);
  }

  getList(): Observable<Vote[]> {
    return this.apiClient.getAllVotes().pipe(
      map(votes => {
        return votes.map(vote => {
          return vote;
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
