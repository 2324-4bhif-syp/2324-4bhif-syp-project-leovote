import {Component, Input} from '@angular/core';
import {Vote} from "../shared/entity/vote-model";
import {Election} from "../shared/entity/election-model";
import {Candidate} from "../shared/entity/candidate-model";
import {ElectionService} from "../shared/control/election.service";
import {CandidateService} from "../shared/control/candidate.service";
import {VoteService} from "../shared/control/vote.service";

@Component({
  selector: 'app-vote-list',
  templateUrl: './vote-list.component.html',
  styleUrls: ['./vote-list.component.css']
})
export class VoteListComponent {
  @Input() doEdit: boolean = false;
  votes: Vote[] = [];
  elections: Election[] = [];
  candidates: Candidate[] = [];

  selectedElection: Election = new Election(null, "", new Date(), new Date(), "");
  selectedCandidate: Candidate = new Candidate(null, "", "", "", "");
  createVote: Vote = new Vote(this.selectedCandidate, this.selectedElection);
  constructor(
    private electionService: ElectionService,
    private candidateService: CandidateService,
    private voteService: VoteService) {}
  ngOnInit(): void {
    this.loadElections();
    this.electionService.onListRefresh().subscribe(() => {
      this.loadElections();
    });
    this.loadCandidates();
    this.candidateService.onListRefresh().subscribe(() => {
      this.loadCandidates();
    });
    this.loadVotes();
    this.voteService.onListRefresh().subscribe(() => {
      this.loadVotes();
    });
  }
  initVote(){
    this.selectedElection = new Election(null, '', new Date(), new Date(), '');
    this.selectedCandidate = new Candidate(null, "", "", "", "");
    this.createVote = new Vote(this.selectedCandidate, this.selectedElection);
  }
  create(){
    this.doEdit = false;
    this.createVote.election = this.selectedElection;
    this.createVote.candidate = this.selectedCandidate;
    this.voteService.add(this.createVote).subscribe(
      response => {
        console.log('Vote added successfully', response);
        this.voteService.refreshList();
      },
      error => {
        console.error('Error adding vote', error);
      }
    );
  }
  private loadElections() {
    this.electionService.getList().subscribe(
      elections => {
        this.elections = elections;
      },
      error => {
        console.error(error);
      }
    );
  }
  private loadCandidates() {
    this.candidateService.getList().subscribe(
      candidates => {
        this.candidates = candidates;
      },
      error => {
        console.error(error);
      }
    );
  }
  private loadVotes() {
    this.voteService.getList().subscribe(
      votes => {
        this.votes = votes;
      },
      error => {
        console.error(error);
      }
    );
  }
}
