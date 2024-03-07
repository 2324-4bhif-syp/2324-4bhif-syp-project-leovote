package at.htlleonding.boundary;

import at.htlleonding.control.TokenService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/token")
public class TokenResource {
    @Inject
    @RestClient
    TokenService tokenService;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String getToken() {
        String clientId = "htlleonding-service";
        String clientSecret = "AkIRaaboJ23Q64jSjtN9gkmfMumUybD8";
        String grantType = "password";
        String username = ""; // Replace with actual username
        String password = ""; // Replace with actual password
        String scope = "openid";

        // Call the getToken() method of TokenService interface with parameters
        return tokenService.getToken(clientId, clientSecret, grantType, username, password, scope);
    }
}
