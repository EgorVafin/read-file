package org.example.util.flash;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class FlashMessage implements Serializable {
    static final long srialVersionUID = 42L;

    public final String message;
    private final Type type;

    public enum Type {
        SUCCESS, ERROR
    }
}
