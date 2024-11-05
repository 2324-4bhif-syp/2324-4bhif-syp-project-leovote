import {Component, Input, OnInit} from '@angular/core';
import { VoteService } from '../shared/control/vote.service';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginModel } from "../shared/entity/login-model";
import { KeycloakService } from "keycloak-angular";
import { JwtHelperService } from '@auth0/angular-jwt';
import { Election } from "../shared/entity/election-model";
import { Vote } from "../shared/entity/vote";
import { ElectionService } from "../shared/control/election.service";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  @Input() code: string = "";
  DeniedToVote: boolean = false;
  authFail: boolean = false;
  username: string = "";
  password: string = "";
  user: LoginModel | undefined = undefined;
  election: Election | undefined;

  horizontalPosition: MatSnackBarHorizontalPosition = 'end';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

  constructor(
    public voteService: VoteService,
    private router: Router,
    private route: ActivatedRoute,
    private keycloakService: KeycloakService,
    public electionService: ElectionService,
  ) {}

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
      const helper = new JwtHelperService();
      const val = await helper.decodeToken(this.keycloakService.getToken());
      const email = this.keycloakService.getKeycloakInstance().profile?.email || '';

      const success = await this.voteService.checkCode(this.code);
      const checkEmailAndCode = await this.voteService.checkEmailAndCode(email, this.code);
      const voter: Vote | undefined = await this.voteService.getVoter(email, this.code);

      if (success && checkEmailAndCode && voter !== undefined) {
        this.electionService.getById(voter.participatingIn).subscribe({
          next: (election) => {
            this.election = election;

            let electionStart = new Date(election.electionStart);
            let electionEnd = new Date(election.electionEnd);
            if(electionStart > new Date() || electionEnd < new Date()) {
              this.DeniedToVote = true;
              return;
            }
            if (election.electionType === "CROSSES") {
              this.router.navigate(['/cross-votes']);
            } else if (election.electionType === "MULTIVALUE") {
              this.router.navigate(['/multivalue-votes']);
            }
          },
          error: (err) => {
            console.error('Error fetching election by ID:', err);
            this.DeniedToVote = true;

          }
        });
      } else {
        this.DeniedToVote = true;
      }
    } catch (error) {
      console.error('Error while checking code:', error);
      this.DeniedToVote = true;
    }
  }
}
