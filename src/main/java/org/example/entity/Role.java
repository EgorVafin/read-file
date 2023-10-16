package org.example.entity;

public enum Role {

    ROLE_ADMIN("Администратор"),
    ROLE_USER("Пользователь");

    private final String label;

    Role(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
