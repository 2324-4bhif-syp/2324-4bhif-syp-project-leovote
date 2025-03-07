import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {VoteService} from "./shared/control/vote.service";
import {AdminService} from "./shared/control/admin.service";
import {KeycloakService} from "keycloak-angular";
import {TranslateService} from "@ngx-translate/core";
import {NavbarService} from "./shared/control/navbar.service";
import {MatSidenav} from "@angular/material/sidenav";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements AfterViewInit, OnInit{
  title = 'Leovote';
  currentLanguage = 'en';

  @ViewChild('sidenav') sidenav!: MatSidenav;

  constructor(public voteService: VoteService,
              private router: Router,
              protected adminService: AdminService,
              private keycloakService: KeycloakService,
              private translate: TranslateService,
              private navbarService: NavbarService,
              private cdr: ChangeDetectorRef) {
    const savedLanguage = localStorage.getItem('language') || 'en';
    this.currentLanguage = savedLanguage;
    this.translate.setDefaultLang(savedLanguage);
    this.translate.use(savedLanguage);
  }

  setLanguage(language: string) {
    this.currentLanguage = language;
    this.translate.use(language);
    localStorage.setItem('language', language);
  }

  async ngOnInit() {
    this.navbarService.toggle$.subscribe(() => {
      this.sidenav.toggle();
    });
  }

  ngAfterViewInit() {
    this.cdr.detectChanges();
  }

  /*
  ngOnInit(): void {
    if (!this.voteService.isLoggedIn) {
      this.router.navigate(['/']);
    }
  }*/
  logout() {
    this.keycloakService.logout();
  }

  toggleSidenav() {
    this.navbarService.toggleSidenav();
  }
}
