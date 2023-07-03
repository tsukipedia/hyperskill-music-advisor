package advisor.model;

public class User {
    private String code;
    private Boolean isAuthenticated;

    public User() {
        this.isAuthenticated = false;
    }

    public Boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(Boolean authentication) {
        isAuthenticated = authentication;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (code != null) this.code = code;
    }
}
