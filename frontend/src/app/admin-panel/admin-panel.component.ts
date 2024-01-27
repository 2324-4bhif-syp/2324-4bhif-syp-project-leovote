import {Component} from '@angular/core';
import {ElectionService} from "../shared/control/election.service";
import {Election} from "../shared/entity/election-model";
import {Result} from "../shared/entity/result";
import {EmailModel} from "../shared/entity/email-model";

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent {
}
