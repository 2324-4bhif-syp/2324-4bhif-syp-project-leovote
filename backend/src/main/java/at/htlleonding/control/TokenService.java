package at.htlleonding.control;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/tokens")
@RegisterRestClient(configKey = "token-api")
public interface TokenService {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Path("/token")
    String getToken(@FormParam("client_id") String clientId,
                 @FormParam("client_secret") String clientSecret,
                 @FormParam("grant_type") String grantType,
                 @FormParam("username") String username,
                 @FormParam("password") String password,
                 @FormParam("scope") String scope);
}
