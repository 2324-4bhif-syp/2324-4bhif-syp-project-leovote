import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {FormsModule} from "@angular/forms";
import { CandidateListComponent } from './candidate-list/candidate-list.component';
import { ElectionListComponent } from './election-list/election-list.component';
import {HttpClientModule} from "@angular/common/http";
import { LeovotePageComponent } from './leovote-page/leovote-page.component';
import {RouterModule, Routes} from "@angular/router";
import { ElectionComponent } from './election/election.component';
import { CandidateComponent } from './candidate/candidate.component';
import { LoginComponent } from './login/login.component';
import { VoteComponent } from './vote/vote.component';

const appRoutes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: "full"},
  {path: 'login', component: LoginComponent},
  {path: 'elections', component: ElectionListComponent},
  {path: 'candidates', component: CandidateListComponent},
]
@NgModule({
  declarations: [
    AppComponent,
    CandidateListComponent,
    ElectionListComponent,
    LeovotePageComponent,
    ElectionComponent,
    CandidateComponent,
    LoginComponent,
    VoteComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
