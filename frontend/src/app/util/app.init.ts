import {KeycloakService} from "keycloak-angular";
import {environment} from "../../environments/environment";

export function initializeKeycloak(keycloak: KeycloakService): () => Promise<boolean> {
  return () =>
    keycloak.init({
      config: {
        url: 'https://auth.htl-leonding.ac.at',
        //url: 'http://localhost:8180',
        realm: 'htlleonding',
        //realm:'testing',
        //clientId: 'frontend'
        clientId: environment.clientId,
        //clientId: 'leovote' for production
        //clientId: 'leovotetest' for local development
      },
      initOptions: {
        checkLoginIframe: false,
        checkLoginIframeInterval: 20,
        onLoad: 'login-required'
      },
      loadUserProfileAtStartUp: true
    });
}

