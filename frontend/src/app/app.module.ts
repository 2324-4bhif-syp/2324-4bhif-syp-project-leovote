import {APP_INITIALIZER, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {LeovotePageComponent} from './leovote-page/leovote-page.component';
import {RouterModule, Routes} from "@angular/router";
import {LoginComponent} from './login/login.component';
import {VoteComponent} from './vote/vote.component';
import {AdminPanelComponent} from './admin-panel/admin-panel.component';
import {ResultsComponent} from './results/results.component';
import {CreateCandidateComponent} from './create-candidate/create-candidate.component';
import {CreateElectionComponent} from './create-election/create-election.component';
import {NgOptimizedImage} from "@angular/common";
import {EmailsComponent} from './emails/emails.component';
import {AuthGuard} from "./util/app.guard";
import {KeycloakAngularModule, KeycloakService} from "keycloak-angular";
import {initializeKeycloak} from "./util/app.init";

const appRoutes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: "full"},
  {path: 'login', component: LoginComponent, canActivate: [AuthGuard]},
  {path: 'votes', component: VoteComponent},
  {
    path: 'admin', component: AdminPanelComponent, children: [
      {path: 'results', component: ResultsComponent},
      {path: 'create-candidate', component: CreateCandidateComponent},
      {path: 'create-election', component: CreateElectionComponent},
      {path: 'emails', component: EmailsComponent}
    ]
  }
]

@NgModule({
  declarations: [
    AppComponent,
    LeovotePageComponent,
    LoginComponent,
    VoteComponent,
    AdminPanelComponent,
    ResultsComponent,
    CreateCandidateComponent,
    CreateElectionComponent,
    EmailsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    NgOptimizedImage,
    KeycloakAngularModule,
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
