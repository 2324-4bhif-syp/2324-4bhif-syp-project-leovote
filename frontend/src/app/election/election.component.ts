import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Candidate} from "../shared/entity/candidate-model";
import {Election} from "../shared/entity/election-model";

@Component({
  selector: 'app-election',
  templateUrl: './election.component.html',
  styleUrls: ['./election.component.css']
})
export class ElectionComponent {
  @Input() doEdit: boolean = false;
  @Input() election: Election = new Election(null, "", new Date(), new Date(), "");
  createElection: Election = new Election(null, "", new Date(), new Date(), "");
  @Output() editDone = new EventEmitter<Election>();
  @Input() isError: boolean = false;

  create(){
    if(this.isEndDateValid(this.createElection.electionStart, this.createElection.electionEnd)){
      this.doEdit = false;
      this.isError = false;
      this.editDone.emit(this.createElection);
      this.election = this.createElection;
      this.createElection = new Election(null, "", new Date(), new Date(), "");
    } else {
      console.error('End date must be equal to or after start date.');
      this.isError = true;
    }
  }
  parseDate(eventData: Event){
    const dateString = (eventData.target as HTMLInputElement).value;
    if(dateString){
      return new Date(dateString);
    }
    return new Date();
  }
  isEndDateValid(startDate: Date, endDate: Date): boolean {
    const startDay = new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate());
    const endDay = new Date(endDate.getFullYear(), endDate.getMonth(), endDate.getDate());
    return !endDate || !startDate || endDay >= startDay;
  }
}
