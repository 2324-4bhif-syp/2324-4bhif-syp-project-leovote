import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { CdkDragDrop, transferArrayItem, moveItemInArray } from '@angular/cdk/drag-drop';
import { Vote } from "../shared/entity/vote";
import { Election } from "../shared/entity/election-model";
import { Candidate } from "../shared/entity/candidate-model";
import { CandidateImage } from "../shared/entity/candidate-image";
import { VoteService } from "../shared/control/vote.service";
import { ElectionService } from "../shared/control/election.service";
import { KeycloakService } from "keycloak-angular";
import { CandidateService } from "../shared/control/candidate.service";
import {CandidateVoteDto} from "../shared/entity/dto/candidate-vote-dto";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-multivalue-vote',
  templateUrl: './multivalue-vote.component.html',
  styleUrls: ['./multivalue-vote.component.css'],
})
export class MultivalueVoteComponent implements OnInit {
  @ViewChild('confirmationDialog') confirmationDialog!: TemplateRef<any>;

  isVoted: boolean = false;
  voter: Vote | undefined = this.voteService.vote;
  election: Election | undefined = undefined;
  candidates: CandidateImage[] = [];
  electionInFuture: boolean = true;

  points = [6, 5, 4, 3, 2, 1];
  ratedCandidates: Candidate[][] = [[], [], [], [], [], []];
  ratingListIds = this.points.map((_, i) => `rating-list-${i}`);
  allDropLists = ['candidates-list', ...this.ratingListIds];

  constructor(
    public voteService: VoteService,
    public electionService: ElectionService,
    public keycloakService: KeycloakService,
    public candidateService: CandidateService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.candidateService.candidateImage().subscribe((candidateImages) => {
      this.candidates = candidateImages;
      this.electionService.getList().subscribe((elections) => {
        this.election = elections.find((e) => e.id === this.voter?.participatingIn);
        this.linkCandidatesToElection();
        this.checkDate();
      });
    });
  }

  openConfirmationDialog() {
    const dialogRef = this.dialog.open(this.confirmationDialog);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
      }
    });
  }

  confirmVote() {
    this.dialog.closeAll();
    this.voting();
  }

  cancelVote() {
    this.dialog.closeAll();
  }

  voting() {
    if (this.election?.id != undefined) {
      let candidateVoteDto: CandidateVoteDto[] = [];
      this.ratedCandidates.forEach((candidates, index) => {
        const points = this.points[index];
        candidates.forEach(candidate => {
          candidateVoteDto.push(new CandidateVoteDto(candidate.id!, points));
        });
      });
      this.voteService.voteCallForAll(candidateVoteDto, this.election.id);
      this.voteService.isLoggedIn = false;
      this.isVoted = true;
    }
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

  checkDate() {
    const now = new Date();
    if (this.election == undefined) {
      console.log("undefined");
    }
    this.electionInFuture = this.election != undefined && this.election.electionEnd > now;
  }

  getBase64Image(base64String: string | undefined): string {
    return `data:image/jpeg;base64,${base64String}`;
  }

  // Handling dragging within or across lists
  drop(event: CdkDragDrop<Candidate[] | undefined>) {
    if (event.container.data !== undefined && event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    }
    this.logCurrentState();
  }

  // Moving candidates between the list and rating boxes
  dropToRating(event: CdkDragDrop<Candidate[], any>, index: number) {
    const previousData = event.previousContainer.data as Candidate[];
    const currentData = event.container.data as Candidate[];

    if (previousData && currentData) {
      // Prevent adding more than one candidate to the rating box
      if (this.ratedCandidates[index].length === 0) {
        transferArrayItem(
          previousData,
          this.ratedCandidates[index],
          event.previousIndex,
          event.currentIndex
        );
      }
    }

    // Handle returning the candidate to the main list
    if (previousData && event.container.id === 'candidates-list' && this.election?.participatingCandidates) {
      transferArrayItem(
        previousData,
        this.election.participatingCandidates,
        event.previousIndex,
        event.currentIndex
      );
    }
    this.logCurrentState();
  }

  logCurrentState() {
    console.log("Candidates in the election:");
    this.election?.participatingCandidates.forEach(candidate => {
      console.log(`${candidate.firstName} ${candidate.lastName}`);
    });
    console.log("Rated Candidates with Points:");
    this.ratedCandidates.forEach((candidates, index) => {
      console.log(`Points ${this.points[index]}:`);
      candidates.forEach(candidate => {
        console.log(` - ${candidate.firstName} ${candidate.lastName}`);
      });
    });
  }
}
