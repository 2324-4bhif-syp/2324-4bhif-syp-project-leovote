package at.htlleonding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfoResponse {
    @JsonProperty("sub")
    private String sub;
    @JsonProperty("email_verified")
    private boolean emailVerified;
    @JsonProperty("LDAP_ENTRY_DN")
    private String lDapEntry;
    @JsonProperty("name")
    private String name;
    @JsonProperty("preferred_username")
    private String preferredUsername;
    @JsonProperty("given_name")
    private String firstName;
    @JsonProperty("family_name")
    private String familyName;

    public UserInfoResponse() {
    }

    public UserInfoResponse(String sub, boolean emailVerified, String lDapEntry, String name, String preferredUsername, String firstName, String familyName) {
        this.sub = sub;
        this.emailVerified = emailVerified;
        this.lDapEntry = lDapEntry;
        this.name = name;
        this.preferredUsername = preferredUsername;
        this.firstName = firstName;
        this.familyName = familyName;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getlDapEntry() {
        return lDapEntry;
    }

    public void setlDapEntry(String lDapEntry) {
        this.lDapEntry = lDapEntry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}
