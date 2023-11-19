import {Component, Input} from '@angular/core';
import {CandidateService} from "../shared/control/candidate.service";
import {Candidate} from "../shared/entity/candidate-model";
import {Election} from "../shared/entity/election-model";
import {ElectionService} from "../shared/control/election.service";

@Component({
  selector: 'app-candidate-list',
  templateUrl: './candidate-list.component.html',
  styleUrls: ['./candidate-list.component.css']
})
export class CandidateListComponent {
  @Input() doEdit: boolean = false;
  candidates: Candidate[] = [];
  createCandidate: Candidate = this.initCandidate();

  constructor(private candidateService: CandidateService) {}

  ngOnInit(): void {
    this.loadCandidates();
    this.candidateService.onListRefresh().subscribe(() => {
      this.loadCandidates();
    });
  }

  initCandidate(): Candidate {
    return new Candidate("", "", "", "");
  }

  create() {
    this.doEdit = false;
    this.candidateService.add(this.createCandidate).subscribe(
      response => {
        console.log('Candidate added successfully', response);
        this.candidateService.refreshList();
      },
      error => {
        console.error('Error adding candidate', error);
      }
    );
    this.createCandidate = this.initCandidate();
  }
  private loadCandidates() {
    this.candidateService.getList().subscribe(
      candidates => {
        console.log(candidates);
        this.candidates = candidates;
      },
      error => {
        console.error(error);
      }
    );
  }
}
