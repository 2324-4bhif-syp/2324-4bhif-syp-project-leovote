package at.htlleonding.boundary;

import at.htlleonding.control.TokenService;
import at.htlleonding.entity.UserInfoResponse;
import at.htlleonding.entity.dto.LoginDataDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.json.JSONObject;

@Path("token")
public class TokenResource {
    @Inject
    @RestClient
    TokenService tokenService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getToken(LoginDataDTO loginDataDTO) {
        String clientId = "htlleonding-service";
        String clientSecret = "AkIRaaboJ23Q64jSjtN9gkmfMumUybD8"; // Should be asked Prof. how to handle that
        String grantType = "password";
        String username = loginDataDTO.username();
        String password = loginDataDTO.password();
        String scope = "openid";

        // Call the getToken() method of TokenService interface with parameters
        String jsonResponse = tokenService.getToken(clientId, clientSecret, grantType, username, password, scope);

        // Parse the JSON response
        JSONObject jsonObject = new JSONObject(jsonResponse);

        // Extract the access token
        String accessToken = jsonObject.getString("access_token");

        UserInfoResponse userInfo = getUserInfo(accessToken);

        return Response.accepted(userInfo).build();
    }

    private UserInfoResponse getUserInfo(String accessToken) {
        String tokenToUse = "Bearer " + accessToken;
        return tokenService.getUserInfo(tokenToUse);
    }
}
