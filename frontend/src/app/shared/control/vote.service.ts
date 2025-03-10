import {Injectable} from '@angular/core';
import {LeovoteWebApiService} from "../api/leovote-web-api.service";
import {Vote} from "../entity/vote";
import {VoteCandidate} from "../entity/vote-candidate-model";
import {Router} from "@angular/router";
import {LoginModel} from "../entity/login-model";
import {VoteRequestDto} from "../entity/dto/vote-request-dto";
import {CandidateVoteDto} from "../entity/dto/candidate-vote-dto";

@Injectable({
  providedIn: 'root'
})
export class VoteService {
  public vote: Vote | undefined;
  isLoggedIn: boolean = false;
  public user: LoginModel | undefined;

  constructor(private apiClient: LeovoteWebApiService,
              private router: Router) {
  }

  checkCode(code: string): Promise<boolean> {
    return new Promise<boolean>((resolve, reject) => {
      this.apiClient.getVoteByCode(code).subscribe(
        (v: Vote) => {
          this.vote = v;
          if (this.vote !== undefined && !this.vote.voted) {
            this.isLoggedIn = true;
            resolve(true);
          } else {
            resolve(false);
          }
        },
        (error) => {
          console.error('Error fetching vote:', error);
          reject(error);
        }
      );
    });
  }

  checkEmailAndCode(email: string, code: string): Promise<boolean> {
    return new Promise<boolean>((resolve, reject) => {
      this.apiClient.checkEmailAndCode(email, code).subscribe(
        (b: boolean) => {
          if (b) {
            resolve(true);
          } else {
            resolve(false);
          }
        },
        (error) => {
          console.error('Error checking email and code:', error);
          reject(error);
        }
      );
    });
  }
  getVoter(email: string, code: string): Promise<Vote | undefined> {
    return new Promise<Vote | undefined>((resolve, reject) => {
      this.apiClient.checkEmailAndCode(email, code).subscribe(
        (value) => {
          if (value) {
            this.apiClient.getVoteByCode(code).subscribe(
              (vote: Vote) => {
                this.vote = vote;
                resolve(vote);
              },
              (error) => {
                console.error('Error fetching vote:', error);
                reject(error);
              }
            );
          } else {
            resolve(undefined)
          }
        },
        (error) => {
          console.error('Error checking email and code:', error);
          reject(error)
        }
      );
    });
  }

  voteCall(selectedCandidate: number, electionId: number) {
    if (this.vote?.generatedId != undefined) {
      let voteCandidate: VoteCandidate = new VoteCandidate(this.vote.generatedId)
      console.log(voteCandidate);
      if(selectedCandidate) {
        let candidateVote: CandidateVoteDto[] = [];
        candidateVote.push(new CandidateVoteDto(selectedCandidate, 1))
        let voteRequest: VoteRequestDto = new VoteRequestDto(this.vote.generatedId, candidateVote)
        this.apiClient.voteForCandidate(electionId, voteRequest).subscribe(
          () => {
            console.log('Vote successful');
            this.isLoggedIn = false;
          },
          (error) => {
            console.error('Error in voting');
          }
        );
      }
    } else {
      console.log("No Id given");
    }
  }
  voteCallForAll(candidates: CandidateVoteDto[], electionId: number) {
    if (this.vote?.generatedId != undefined) {
      let voteRequest: VoteRequestDto = new VoteRequestDto(this.vote.generatedId, candidates)
      console.log("Candidates: " + candidates);
      this.apiClient.voteForCandidate(electionId, voteRequest).subscribe(
        () => {
          console.log('Vote successful');
          this.isLoggedIn = false;
        },
        (error) => {
          console.error('Error in voting');
        }
      );

    } else {
      console.log("No Vote Id given");
    }
  }
}
