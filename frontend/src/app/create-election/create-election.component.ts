import {Component, OnInit} from '@angular/core';
import {Candidate} from "../shared/entity/candidate-model";
import {Election} from "../shared/entity/election-model";
import {CandidateService} from "../shared/control/candidate.service";
import {ElectionService} from "../shared/control/election.service";

@Component({
  selector: 'app-create-election',
  templateUrl: './create-election.component.html',
  styleUrls: ['./create-election.component.css']
})
export class CreateElectionComponent implements OnInit {
  selectedCandidates: Candidate[] = [];
  availableCandidates: Candidate[] = [];
  elections: Election[] = [];
  filteredCandidates: Candidate[] = [];
  searchText: string = '';
  election: Election = new Election(
    null,
    "",
    new Date(),
    new Date(),
    "",
    "", [], null)

  ngOnInit(): void {
    this.loadAvailableCandidates();
  }

  filterCandidates() {
    if (this.searchText.trim() === '') {
      // If search text is empty, show all candidates
      this.filteredCandidates = this.availableCandidates;
    } else {
      // If search text is not empty, filter candidates based on it
      this.filteredCandidates = this.availableCandidates.filter(candidate =>
        candidate.firstName.toLowerCase().includes(this.searchText.toLowerCase()) ||
        candidate.lastName.toLowerCase().includes(this.searchText.toLowerCase())
      );
    }
  }

  loadAvailableCandidates(): void {
    this.candidateService.getList().subscribe(
      candidates => {
        this.availableCandidates = candidates;
        this.filteredCandidates = this.availableCandidates;
      },
      error => console.error('Error loading candidates:', error)
    );
  }

  constructor(
    protected candidateService: CandidateService,
    protected electionService: ElectionService
  ) {
    electionService.getList().forEach(value => {
      this.elections = value;
    });
  }

  toggleCandidateSelection(candidate: Candidate) {
    const index = this.selectedCandidates.findIndex(selected => selected.id === candidate.id);
    if (index !== -1) {
      this.selectedCandidates.splice(index, 1);
    } else {
      this.selectedCandidates.push(candidate);
    }
  }

  isCreateButtonDisabled(): boolean {
    const isMaxPointsRequired = this.election.electionType === 'MULTIVALUE';
    return !this.election.name ||
      !this.election.electionType ||
      !this.election.electionStart ||
      !this.election.electionEnd ||
      (isMaxPointsRequired && !this.election.maxPoints) || // Ensure maxPoints is specified for MULTIVALUE
      this.selectedCandidates.length < 2 ||
      this.election.electionEnd <= this.election.electionStart; // Ensure End is after Start
  }

  createElection() {
    this.election.participatingCandidates = this.selectedCandidates;
    if(this.election.electionType === 'CROSSES'){
      this.election.maxPoints = 1;
    }
    this.electionService.add(this.election).subscribe(
      (response) => {
        console.log('Election created successfully:', response);
        this.elections.push(response);
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
      "", [], null)
  }

  isSelected(candidate: Candidate): boolean {
    return this.selectedCandidates.some(selected => selected.id === candidate.id);
  }

  confirmDelete(election: Election) {
    if (confirm(`Are you sure you want to delete Election: ${election.name} ?`)) {
      this.deleteElection(election);
    }
  }

  deleteElection(election: Election): void {
    if(confirm(`Do you want to delete election ${election.name}?`))
    if (election.id !== null) {
      this.electionService.delete(election.id.toString()).subscribe(
        () => {
          console.log('Election deleted successfully');
          this.elections = this.elections.filter(e => e.id !== election.id);
        },
        error => {
          console.error('Error deleting election:', error);
        }
      );
    }
  }
}
