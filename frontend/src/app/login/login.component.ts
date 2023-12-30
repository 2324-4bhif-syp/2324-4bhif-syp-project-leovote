import {Component, Input} from '@angular/core';
import {LeovoteWebApiService} from "../shared/api/leovote-web-api.service";
import {VoteService} from "../shared/control/vote.service";
import {Vote} from "../shared/entity/vote";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  @Input() code: string = "";

  constructor(public voteService: VoteService) {}
  isCodeNotUsedOrIncorrect() {
    console.log("HELLO");
    this.voteService.checkCode(this.code);
  }
}
