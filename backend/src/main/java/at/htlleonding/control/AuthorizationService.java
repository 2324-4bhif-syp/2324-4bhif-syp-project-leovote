package at.htlleonding.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class AuthorizationService {
    private static final String CLAIM = "ldap_entry_dn";
    private static final String RBAC_ENABLED = "security.access-control.enabled";

    @Inject
    Config config;

    public boolean hasAccess(JsonWebToken jwt, String requiredOU) {
        boolean accessControlEnabled = config.getValue(RBAC_ENABLED, Boolean.class);

        if(accessControlEnabled) {
            String ldapEntry = jwt.getClaim(CLAIM);
            return ldapEntry != null && ldapEntry.contains(requiredOU);
        }
        return true;
    }
}