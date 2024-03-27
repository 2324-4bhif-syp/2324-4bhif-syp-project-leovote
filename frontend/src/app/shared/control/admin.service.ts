import {Injectable} from '@angular/core';
import {KeycloakService} from "keycloak-angular";

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  isLoggedIn: boolean = this.keycloakService.getUserRoles().includes('default-roles-master');

  constructor(private keycloakService: KeycloakService) {
  }
}
