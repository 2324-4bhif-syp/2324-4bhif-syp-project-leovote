import {Injectable} from '@angular/core';
import {Election} from "../entity/election-model";
import {LeovoteWebApiService} from "../api/leovote-web-api.service";
import {catchError, map, Observable, Subject, throwError} from "rxjs";
import {EmailModel} from "../entity/email-model";

@Injectable({
  providedIn: 'root'
})
export class ElectionService {
  constructor(private apiClient: LeovoteWebApiService) {
  }

  private refreshListSubject = new Subject<void>();

  add(election: Election): Observable<any> {
    return this.apiClient.addElection(election);
  }

  addEmail(email: string, electionId: string) {
     return this.apiClient.addEmail(email, electionId)    .pipe(
       catchError((error) => {
         // Handle the error here
         console.error("Input Email is not a Email");
         // Rethrow the error so that the calling code can also handle it if needed
         return error;
       })
     );
  }

  getMails(electionId: string): Observable<EmailModel[]>{
    return this.apiClient.getAllMails(electionId);
  }

  removeMail(mailId: string) {
    return this.apiClient.removeMail(mailId);
  }

  result(id: string): Observable<Object> {
      return this.apiClient.getResultByElection(id);
  }

  getList(): Observable<Election[]> {
    return this.apiClient.getAllElections().pipe(
      map(elections => {
        return elections.map(election => {
          election.electionStart = new Date((election.electionStart as unknown) as string);
          election.electionEnd = new Date((election.electionEnd as unknown) as string);
          return election;
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
