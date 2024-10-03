import {KeycloakService} from "keycloak-angular";
import Keycloak from "keycloak-js";
import {environment} from "../../environments/environment";

export function initializeKeycloak(keycloak: KeycloakService): () => Promise<boolean> {
  return () =>
    keycloak.init({
      config: {
        url: 'https://auth.htl-leonding.ac.at',
        realm: 'htl-leonding',
        clientId: environment.clientId,
        //clientId: 'leovote' for production
        //clientId: 'leovotetest' for local development
      },
      initOptions: {
        checkLoginIframe: true,
        checkLoginIframeInterval: 20
      },
      loadUserProfileAtStartUp: true
    });
}

