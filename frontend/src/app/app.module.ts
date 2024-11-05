import {APP_INITIALIZER, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {RouterModule, Routes} from "@angular/router";
import {LoginComponent} from './login/login.component';
import {CrossVoteComponent} from './cross-vote/cross-vote.component';
import {AdminPanelComponent} from './admin-panel/admin-panel.component';
import {ResultsComponent} from './results/results.component';
import {CreateCandidateComponent} from './create-candidate/create-candidate.component';
import {CreateElectionComponent} from './create-election/create-election.component';
import {NgOptimizedImage} from "@angular/common";
import {EmailsComponent} from './emails/emails.component';
import {AuthGuard} from "./util/app.guard";
import {KeycloakAngularModule, KeycloakService} from "keycloak-angular";
import {initializeKeycloak} from "./util/app.init";
import { AdminElectionListComponent } from './admin-election-list/admin-election-list.component';
import { CandidateOverviewComponent } from './candidate-overview/candidate-overview.component';
import { CandidateUpdateComponent } from './candidate-update/candidate-update.component';
import { MultivalueVoteComponent } from './multivalue-vote/multivalue-vote.component';
import {CdkDrag, CdkDragPreview, CdkDropList} from "@angular/cdk/drag-drop";
import {CdkListbox} from "@angular/cdk/listbox";
import {MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {MatCardModule} from "@angular/material/card";

const appRoutes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: "full"},
  {path: 'login', component: LoginComponent, canActivate: [AuthGuard]},
  {path: 'cross-votes', component: CrossVoteComponent},
  {path: 'multivalue-votes', component: MultivalueVoteComponent},
  {
    path: 'admin', component: AdminPanelComponent, canActivate: [AuthGuard], children: [
      {path: 'overview', component: AdminElectionListComponent},
      {path: 'overview/results', component: ResultsComponent},
      {path: 'overview/create-candidate', component: CreateCandidateComponent},
      {path: 'overview/create-election', component: CreateElectionComponent},
      {path: 'overview/emails', component: EmailsComponent},
      {path: 'overview/candidate-update', component: CandidateUpdateComponent},
      {path: 'overview/candidate-overview', component: CandidateOverviewComponent}
    ]
  }
]

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CrossVoteComponent,
    AdminPanelComponent,
    ResultsComponent,
    CreateCandidateComponent,
    CreateElectionComponent,
    EmailsComponent,
    AdminElectionListComponent,
    CandidateOverviewComponent,
    CandidateUpdateComponent,
    MultivalueVoteComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    NgOptimizedImage,
    KeycloakAngularModule,
    CdkDropList,
    CdkListbox,
    CdkDragPreview,
    CdkDrag,
    MatDialogModule,
    MatButtonModule,
    MatCardModule
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
