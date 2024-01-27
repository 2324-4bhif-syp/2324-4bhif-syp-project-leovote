import {Component, OnInit} from '@angular/core';
import { Candidate } from "../shared/entity/candidate-model";
import { Election } from "../shared/entity/election-model";
import { CandidateService } from "../shared/control/candidate.service";
import { ElectionService } from "../shared/control/election.service";

@Component({
  selector: 'app-create-election',
  templateUrl: './create-election.component.html',
  styleUrls: ['./create-election.component.css']
})
export class CreateElectionComponent implements OnInit{
  selectedCandidates: Candidate[] = [];
  availableCandidates: Candidate[] = [];

  election: Election = new Election(
    null,
    "",
    new Date(),
    new Date(),
    "",
    "", [])

  ngOnInit(): void {
    this.loadAvailableCandidates();
  }

  loadAvailableCandidates(): void {
    this.candidateService.getList().subscribe(
      candidates => {
        this.availableCandidates = candidates;
      },
      error => console.error('Error loading candidates:', error)
    );
  }
  constructor(
    protected candidateService: CandidateService,
    private electionService: ElectionService
  ) {
    console.log(this.candidateService.getList())
  }

  toggleCandidateSelection(candidate: Candidate) {
    const index = this.selectedCandidates.findIndex(selected => selected.id === candidate.id);
    if (index !== -1) {
      this.selectedCandidates.splice(index, 1);
    } else {
      this.selectedCandidates.push(candidate);
    }
  }

  createElection() {
    this.election.participatingCandidates = this.selectedCandidates;
    this.electionService.add(this.election).subscribe(
      (response) => {
        console.log('Election created successfully:', response);
        this.selectedCandidates = [];
      },
      (error) => {
        console.error('Error creating election:', error);
      }
    );
    this.election = new Election(
      null,
      "",
      new Date(),
      new Date(),
      "",
      "", [])
  }
  isSelected(candidate: Candidate): boolean {
    return this.selectedCandidates.some(selected => selected.id === candidate.id);
  }
}
