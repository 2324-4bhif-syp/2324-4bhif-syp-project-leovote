import {Component, OnInit} from '@angular/core';
import {Candidate} from "../shared/entity/candidate-model";
import {Election} from "../shared/entity/election-model";
import {CandidateService} from "../shared/control/candidate.service";
import {ElectionService} from "../shared/control/election.service";
import {TranslateService} from '@ngx-translate/core';
import {TablePagination} from "../shared/service/TablePaginationService";


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

  sampleCandidates = [
    {name: "Adam", class: "1CHIF"},
    {name: "Something", class: "1BHIF"},
    {name: "Alan", class: "3CHIF"},
    {name: "Echo", class: "4BHIF"},
    {name: "Charlie", class: "2CHIF"},
    // Add more rows here as needed
  ];

  tablePaginationService = new TablePagination(this.sampleCandidates, 10)


  ngOnInit(): void {
    this.loadAvailableCandidates();
  }

  loadAvailableCandidates(): void {
    this.candidateService.getList().subscribe(
      candidates => {
        this.availableCandidates = candidates;
        this.filteredCandidates = this.availableCandidates;
        this.tablePaginationService = new TablePagination(this.availableCandidates, 8)
      },
      error => console.error('Error loading candidates:', error)
    );
  }

  constructor(
    protected candidateService: CandidateService,
    protected electionService: ElectionService,
    private translate: TranslateService
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
    if (this.election.electionType === 'CROSSES') {
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
    const message = this.translate.instant('confirm_delete_election', {name: election.name});
    if (confirm(message)) {
      this.deleteElection(election);
    }
  }

  deleteElection(election: Election): void {
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

  filterCandidates(): void {
    const search = this.searchText.trim().toLowerCase();
    this.filteredCandidates = this.availableCandidates.filter(candidate =>
      candidate.firstName.toLowerCase().includes(search) ||
      candidate.lastName.toLowerCase().includes(search) ||
      candidate.grade.toLowerCase().includes(search)
    );
    this.tablePaginationService = new TablePagination(this.filteredCandidates, 8);
  }
}
