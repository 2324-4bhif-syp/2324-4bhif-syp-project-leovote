import {Injectable} from '@angular/core';
import {KeycloakService} from "keycloak-angular";
import {JwtHelperService} from "@auth0/angular-jwt";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  //isLoggedIn: boolean = this.keycloakService.getUserRoles().includes('default-roles-master');
  isLoggedIn: Promise<boolean> = this.keycloakService.isLoggedIn();

  constructor(private keycloakService: KeycloakService) {
    const helper = new JwtHelperService();
    const val = helper.decodeToken(this.keycloakService.getToken())
    val.then(value => {
      const ldap = value['ldap_entry_dn'];
      if (environment.rbac) {
        this.isLoggedIn = ldap.includes("Teachers");
      }
    });
  }
}
