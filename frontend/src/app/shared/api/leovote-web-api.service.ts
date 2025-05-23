import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Candidate} from "../entity/candidate-model";
import {Election} from "../entity/election-model";
import {Vote} from "../entity/vote";
import {EmailModel} from "../entity/email-model";
import {CandidateImage} from "../entity/candidate-image";
import {environment} from "../../../environments/environment";
import {VoteRequestDto} from "../entity/dto/vote-request-dto";

@Injectable({
  providedIn: 'root'
})
export class LeovoteWebApiService {

  headers = new HttpHeaders().set('Accept', 'application/json');
  //baseUrl = 'http://leovote.htl-leonding.ac.at/api/'; for production
  //baseUrl = 'http://localhost:8080/'; for local development
  private baseUrl = environment.apiUrl;
  private candidates = 'candidates';
  private candidatesAll = 'candidates/all';
  private elections: string = 'elections'; // user
  private addElectionUrl: string = 'elections/election';
  private voters: string = 'voters/voter/${id}'; // user
  private vote: string = 'voters/vote/${electionId}'; // user
  private electionResult: string = 'elections/results/${id}';
  private addEmailUrl: string = 'elections/addEmail/${id}/${email}';
  private addMultipleEmailsUrl: string = 'elections/addEmail/multiples/${electionId}';
  private allMails: string = 'email/${electionId}';
  private removeMailUrl: string = 'elections/removeEmail/${id}';
  private sendCodesUrl: string = 'email/election/${electionId}';
  private removeCandidate: string = 'candidates/${id}';
  private candidateBySchoolId: string = 'candidates/getBySchoolId/${schoolId}';
  private removeElection: string = 'elections/${id}';
  private checkEmailAndCodeUrl: string = 'voters/voter/${email}/${code}'; // user
  private uploadImageUrl: string = 'candidates/images/${id}';
  private getCandidatesAndImages: string = 'candidates/images'; // user
  private getCandidateByIdUrl: string = 'candidates/${id}';
  private getElectionByIdUrl: string = 'elections/${id}'; // user
  private getImageByIdUrl: string = 'candidates/images/${id}';
  private updateCandidateUrl: string = 'candidates/update/${id}';

  constructor(private http: HttpClient) {
  }

  public getAllCandidates() {
    return this.http.get<Candidate[]>(this.baseUrl + this.candidatesAll, {headers: this.headers});
  }

  public getAllElections() {
    return this.http.get<Election[]>(this.baseUrl + this.elections, {headers: this.headers});
  }

  public voteForCandidate(electionId: number, voteRequest: VoteRequestDto) {
    return this.http.post(this.baseUrl + this.vote.replace('${electionId}', electionId.toString()),
      voteRequest, {headers: this.headers});
  }

  public addCandidate(candidate: Candidate) {
    return this.http.post(this.baseUrl + this.candidates, candidate, {headers: this.headers});
  }

  public deleteCandidate(candidateId: string) {
    return this.http.delete(this.baseUrl + this.removeCandidate.replace('${id}', candidateId), {headers: this.headers});
  }

  public addElection(election: Election) {
    return this.http.post(this.baseUrl + this.addElectionUrl, election, {headers: this.headers});
  }

  public deleteElection(electionId: string) {
    return this.http.delete(this.baseUrl + this.removeElection.replace('${id}', electionId), {headers: this.headers});
  }

  public getVoteByCode(code: string) {
    return this.http.get<Vote>(this.baseUrl + this.voters.replace('${id}', code),
      {headers: this.headers});
  }

  public getResultByElection(electionId: string) {
    return this.http.get<Object>(this.baseUrl + this.electionResult.replace('${id}', electionId), {headers: this.headers});
  }

  public addEmail(email: string, electionId: string) {
    return this.http.post(this.baseUrl + this.addEmailUrl.replace('${id}', electionId).replace('${email}', email), {headers: this.headers});
  }

  public getAllMails(electionId: string) {
    return this.http.get<EmailModel[]>(this.baseUrl + this.allMails.replace('${electionId}', electionId), {headers: this.headers});
  }

  public removeMail(mailId: string) {
    return this.http.delete(this.baseUrl + this.removeMailUrl.replace('${id}', mailId), {headers: this.headers});
  }

  public sendCodes(electionId: string) {
    return this.http.post(this.baseUrl + this.sendCodesUrl.replace('${electionId}', electionId), {headers: this.headers});
  }

  public addMultipleEmails(email: string[], electionId: string) {
    return this.http.post(this.baseUrl + this.addMultipleEmailsUrl.replace('${electionId}', electionId), email, {headers: this.headers});
  }

  public checkEmailAndCode(email: string, code: string) {
    return this.http.get<boolean>(this.baseUrl + this.checkEmailAndCodeUrl
      .replace('${email}', email)
      .replace('${code}', code), {headers: this.headers});
  }

  public uploadImage(image: File, id: string) {
    const formData: FormData = new FormData();
    formData.append('image', image, image.name);
    return this.http.post(
      this.baseUrl + this.uploadImageUrl.replace('${id}', id.toString()),formData, {responseType: 'text'}
    );
  }

  public getImagesAndCandidates() {
    return this.http.get<CandidateImage[]>(this.baseUrl + this.getCandidatesAndImages);
  }

  public getCandidateById(id: number) {
    return this.http.get<Candidate>(this.baseUrl + this.getCandidateByIdUrl.replace('${id}', id.toString()), {headers: this.headers});
  }
  public getElectionById(id: number) {
    return this.http.get<Election>(this.baseUrl + this.getElectionByIdUrl.replace('${id}', id.toString()), {headers: this.headers});
  }


  public getImageById(id: number) {
    return this.http.get<CandidateImage>(this.baseUrl + this.getImageByIdUrl.replace('${id}', id.toString()), {headers: this.headers});
  }

  public updateCandidate(candidate: Candidate, id: number) {
    return this.http.put(this.baseUrl + this.updateCandidateUrl.replace('${id}', id.toString()), candidate, {headers: this.headers});
  }

  public getCandidateBySchoolId(schoolId: string){
    return this.http.get<Candidate>(this.baseUrl + this.candidateBySchoolId.replace('${schoolId}', schoolId), {headers: this.headers});
  }
}
