  import { Component, Input } from '@angular/core';
import { VoteService } from '../shared/control/vote.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  @Input() code: string = "";
  alreadyVotedOrIncorrect: boolean = false;
  username: string = "";
  password: string = "";

  constructor(
    public voteService: VoteService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      const token = params.get('token');
      if(token != null){
        this.code = token;
      }
    });
  }

  async checkLogin() {
    try {
      const success = await this.voteService.checkCode(this.code);
      if (success) {
        await this.router.navigate(['/votes']);
      }
      else {
        //console.log("alreadyVotedOrIncorrect is TRUE")
        this.alreadyVotedOrIncorrect = true;
      }
    } catch (error) {
      console.error('Fehler bei der Überprüfung des Codes:', error);
      this.alreadyVotedOrIncorrect = true;
    }
  }
}
