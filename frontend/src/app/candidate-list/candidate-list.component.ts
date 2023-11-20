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
  candidates: Candidate[] =[]
  constructor(private candidateService: CandidateService) {}
  ngOnInit(): void {
    this.loadCandidates();
    this.candidateService.onListRefresh().subscribe(() => {
      this.loadCandidates();
    });
  }
  create(candidate: Candidate) {
    console.log(candidate)
    this.doEdit = false;
    this.candidateService.add(candidate).subscribe(
      response => {
        console.log('Candidate added successfully', response);
        this.candidateService.refreshList();
      },
      error => {
        console.error('Error adding candidate', error);
      }
    );
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
