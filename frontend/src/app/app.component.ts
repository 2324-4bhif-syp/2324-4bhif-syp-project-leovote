import { Component } from '@angular/core';
import {LeovoteWebApiService} from "./shared/api/leovote-web-api.service";
import {Router} from "@angular/router";
import {VoteService} from "./shared/control/vote.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Leovote';
  constructor(public voteService: VoteService, private router: Router) {}
  /*
  ngOnInit(): void {
    if (!this.voteService.isLoggedIn) {
      this.router.navigate(['/']);
    }
  }*/
}
