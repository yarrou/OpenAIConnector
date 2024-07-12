package de.alexkononsol.gptchatapp.connectionUtils;

public enum Role {
    GPT_4O("gpt-4o"),
    GPT_4_TURBO("gpt-4-turbo"),
    GPT_3_5_TURBO_0125("gpt-3.5-turbo-0125");

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
