import {Component, Input} from '@angular/core';
import {VoteService} from '../shared/control/vote.service';
import {ActivatedRoute, Router} from '@angular/router';
import {LoginModel} from "../shared/entity/login-model";
import {KeycloakService} from "keycloak-angular";

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
    private route: ActivatedRoute,
    private keycloakService: KeycloakService
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
      const roleTrue = this.keycloakService.getUserRoles().includes('default-roles-master',0);
      console.log(this.keycloakService.getUserRoles());
      const success = await this.voteService.checkCode(this.code);
      const keycloakInstance = this.keycloakService.getKeycloakInstance();
      let email = '';
      if (keycloakInstance !== undefined
        && keycloakInstance.profile !== undefined
        && keycloakInstance.profile.email !== undefined) {
        email = keycloakInstance.profile.email;
      }
      const checkEmailAndCode = await this.voteService.checkEmailAndCode(email, this.code);
      if (success && roleTrue && checkEmailAndCode) {
        await this.router.navigate(['/votes']);
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
