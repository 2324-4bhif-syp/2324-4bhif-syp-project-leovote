import {Component, OnInit, ViewChild} from '@angular/core';
import {AdminService} from "../shared/control/admin.service";
import {KeycloakService} from "keycloak-angular";
import {NavbarService} from "../shared/control/navbar.service";
import {MatSidenav} from "@angular/material/sidenav";

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent {
  constructor(protected adminService: AdminService,
              private keycloakService: KeycloakService) {
  }
}
