import {Component, Input} from '@angular/core';
import {Router} from "@angular/router";
import {VoteService} from "./shared/control/vote.service";
import {AdminService} from "./shared/control/admin.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Leovote';
  constructor(public voteService: VoteService, private router: Router, protected adminService: AdminService) {}
  /*
  ngOnInit(): void {
    if (!this.voteService.isLoggedIn) {
      this.router.navigate(['/']);
    }
  }*/
}
