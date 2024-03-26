import {KeycloakService} from "keycloak-angular";

export function initializeKeycloak(keycloak: KeycloakService): () => Promise<boolean> {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:9000', // htlleonding not working at the moment, should be changed
        realm: 'master',
        clientId: 'angular-web-client'
      },
      initOptions: {
        checkLoginIframe: true,
        checkLoginIframeInterval: 20
      },
      loadUserProfileAtStartUp: true
    });
}
