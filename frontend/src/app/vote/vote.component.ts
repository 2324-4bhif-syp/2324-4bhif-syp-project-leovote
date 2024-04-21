import {Component, OnInit} from '@angular/core';
import {VoteService} from "../shared/control/vote.service";
import {Vote} from "../shared/entity/vote";
import {Election} from "../shared/entity/election-model";
import {ElectionService} from "../shared/control/election.service";
import {Candidate} from "../shared/entity/candidate-model";
import {KeycloakService} from "keycloak-angular";
import {CandidateService} from "../shared/control/candidate.service";
import {CandidateImage} from "../shared/entity/candidate-image";

@Component({
  selector: 'app-vote',
  templateUrl: './vote.component.html',
  styleUrls: ['./vote.component.css']
})
export class VoteComponent implements OnInit{
  isVoted: boolean = false;
  isSelected: boolean = false;

  voter: Vote | undefined = this.voteService.vote;
  election: Election | undefined = undefined;
  selectedCandidate: Candidate | undefined = undefined;
  candidates: CandidateImage[] = []
  electionInFuture: boolean = true;

  voting() {
    if (this.selectedCandidate?.id != undefined && this.election?.id != undefined) {
      this.voteService.voteCall(this.selectedCandidate.id, this.election.id);
      this.voteService.isLoggedIn = false;
      this.isVoted = true;
    }
  }

  select(){
    this.isSelected = true;
  }

  constructor(
    public voteService: VoteService,
    public electionService: ElectionService,
    public keycloakService: KeycloakService,
    public candidateService: CandidateService
  ) {
  }

  ngOnInit() {
    this.candidateService.candidateImage().subscribe((candidateImages) => {
      this.candidates = candidateImages;
      console.log("Candidates:", this.candidates);
      this.electionService.getList().subscribe((elections) => {
        this.election = elections.find((e) => e.id === this.voter?.participatingIn);
        this.linkCandidatesToElection();
        this.checkDate();
      });
    });
  }

  linkCandidatesToElection() {
    if (this.election && this.candidates.length > 0) {
      this.election.participatingCandidates.forEach((candidate) => {
        const matchingImage = this.candidates.find((c) => c.candidateId === candidate.id);
        if (matchingImage) {
          candidate.pathOfImage = matchingImage.imagePath;
        }
      });
    }
  }

  checkDate(){
    const now = new Date();
    if(this.election == undefined){
      console.log("undefined");
    }
    if (this.election != undefined && this.election.electionEnd > now){
      this.electionInFuture = true;
    } else {
      this.electionInFuture = false;
    }
  }

  deselect() {
    this.isSelected = false;
  }
  getBase64Image(base64String: string | undefined): string {
    return `data:image/jpeg;base64,${base64String}`;
  }
}
