import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import { LeovotePageComponent } from './leovote-page/leovote-page.component';
import {RouterModule, Routes} from "@angular/router";
import { LoginComponent } from './login/login.component';
import { VoteComponent } from './vote/vote.component';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { ResultsComponent } from './results/results.component';

const appRoutes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: "full"},
  {path: 'login', component: LoginComponent},
  {path: 'votes', component: VoteComponent},
  { path: 'admin', component: AdminPanelComponent, children: [
      { path: 'results', component: ResultsComponent },
    ]}
]
@NgModule({
  declarations: [
    AppComponent,
    LeovotePageComponent,
    LoginComponent,
    VoteComponent,
    AdminPanelComponent,
    ResultsComponent
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
