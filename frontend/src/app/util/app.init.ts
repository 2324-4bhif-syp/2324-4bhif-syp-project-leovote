import {KeycloakService} from "keycloak-angular";

export function initializeKeycloak(keycloak: KeycloakService): () => Promise<boolean> {
  return () =>
    keycloak.init({
      config: {
        url: 'https://auth.htl-leonding.ac.at', // htlleonding not working at the moment
        realm: 'htl-leonding',
        clientId: 'token'
      },
      initOptions: {
        checkLoginIframe: true,
        checkLoginIframeInterval: 25
      },
      loadUserProfileAtStartUp: true
    });
}
