import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {FormsModule} from "@angular/forms";
import { CandidateListComponent } from './candidate-list/candidate-list.component';
import { ElectionListComponent } from './election-list/election-list.component';
import {HttpClientModule} from "@angular/common/http";
import { LeovotePageComponent } from './leovote-page/leovote-page.component';
import {RouterModule, Routes} from "@angular/router";
import { VoteListComponent } from './vote-list/vote-list.component';

const appRoutes: Routes = [
  {path: '', redirectTo: 'overview', pathMatch: "full"},
  {path: 'overview', component: LeovotePageComponent},
  {path: 'elections', component: ElectionListComponent},
  {path: 'candidates', component: CandidateListComponent},
  {path: 'votes', component: VoteListComponent}
]
@NgModule({
  declarations: [
    AppComponent,
    CandidateListComponent,
    ElectionListComponent,
    LeovotePageComponent,
    VoteListComponent
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
