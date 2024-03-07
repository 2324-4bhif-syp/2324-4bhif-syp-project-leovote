import {Component, Input} from '@angular/core';
import {VoteService} from '../shared/control/vote.service';
import {ActivatedRoute, Router} from '@angular/router';
import {LoginModel} from "../shared/entity/login-model";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  @Input() code: string = "";
  alreadyVotedOrIncorrect: boolean = false;
  authFail: boolean = false;
  username: string = "";
  password: string = "";
  user: LoginModel | undefined = undefined;

  constructor(
    public voteService: VoteService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      const token = params.get('token');
      if (token != null) {
        this.code = token;
      }
    });
  }

  async checkLogin() {
    try {
      const success = await this.voteService.checkCode(this.code);
      this.user = await this.voteService.checkLoginData(this.username, this.password);
      if(this.user == undefined){
        this.authFail = true;
      }
      if (success) {
        if(this.user !== undefined){
          await this.router.navigate(['/votes']);
        }
      } else {
        //console.log("alreadyVotedOrIncorrect is TRUE")
        this.alreadyVotedOrIncorrect = true;
      }
    } catch (error) {
      console.error('Error while checking code:', error);
      this.alreadyVotedOrIncorrect = true;
    }
  }

}
