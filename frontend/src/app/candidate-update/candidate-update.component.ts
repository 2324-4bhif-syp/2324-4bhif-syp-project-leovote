import {Component, OnInit} from '@angular/core';
import {Election} from "../shared/entity/election-model";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-candidate-update',
  templateUrl: './candidate-update.component.html',
  styleUrls: ['./candidate-update.component.css']
})
export class CandidateUpdateComponent {
  candidateId: number | null = null;

  constructor(
    private route: ActivatedRoute) {
    this.route.queryParamMap.subscribe(params => {
      const id = params.get('candidate');
      if (id != null && Number(id)) {
        this.candidateId = Number(id);
      }
    });
  }

  election: Election | undefined = undefined;
}
