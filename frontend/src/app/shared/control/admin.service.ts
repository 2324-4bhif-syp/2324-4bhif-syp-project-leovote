import {Injectable} from '@angular/core';
import {KeycloakService} from "keycloak-angular";
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  isLoggedIn: boolean = this.keycloakService.getUserRoles().includes('default-roles-master');

  constructor(private keycloakService: KeycloakService) {
    const helper = new JwtHelperService();
    const val = helper.decodeToken(this.keycloakService.getToken())
    val.then(value => {
      const ldap = value['LDAP_ENTRY_DN'];
      //this.isLoggedIn = !ldap.includes("Students");
    });
  }
}
