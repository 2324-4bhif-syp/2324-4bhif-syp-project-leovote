import {Component, Input} from '@angular/core';
import {VoteService} from '../shared/control/vote.service';
import {ActivatedRoute, Router} from '@angular/router';
import {LoginModel} from "../shared/entity/login-model";
import {KeycloakService} from "keycloak-angular";
import {JwtHelperService} from '@auth0/angular-jwt';
import {AdminService} from "../shared/control/admin.service";
import {ElectionService} from "../shared/control/election.service";
import {Election} from "../shared/entity/election-model";
import {Vote} from "../shared/entity/vote";

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
  election: Election | undefined = undefined;

  async checkLogin() {
    try {
      // Check code and user
      let roleTrue: boolean = false;
      const helper = new JwtHelperService();
      const val = helper.decodeToken(this.keycloakService.getToken())
      val.then(value => {
        const ldap = value['LDAP_ENTRY_DN'];
        roleTrue = ldap.includes("Students");
      });
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
