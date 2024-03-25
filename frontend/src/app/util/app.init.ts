import {KeycloakService} from "keycloak-angular";

export function initializeKeycloak(keycloak: KeycloakService): () => Promise<boolean> {
  return () =>
    keycloak.init({
      config: {
        url: 'https://auth.htl-leonding.ac.at/',
        realm: 'htl-leonding',
        clientId: 'angular-web-client'
      },
      initOptions: {
        checkLoginIframe: true,
        checkLoginIframeInterval: 25
      }
    });
}
