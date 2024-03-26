import {Component} from '@angular/core';
import {AdminService} from "../shared/control/admin.service";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent {
  username: string = '';
  password: string = '';

  ngOnInit(){
    this.adminService.isLoggedIn = this.keycloakService.getUserRoles().includes('admin')
  }
  constructor(protected adminService: AdminService, private keycloakService: KeycloakService) {
  }
}
