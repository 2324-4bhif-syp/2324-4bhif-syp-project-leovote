import { Component } from '@angular/core';
import {Election} from "../shared/entity/election-model";

@Component({
  selector: 'app-election',
  templateUrl: './election.component.html',
  styleUrls: ['./election.component.css']
})
export class ElectionComponent {
 election:Election = new Election("0",new Date("01/01/2001"),new Date("01/01/2001"),"Sample");
}
