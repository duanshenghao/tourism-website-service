package com.eastbabel.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.function.BooleanSupplier;

@Getter
@Setter
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 4328407468288938158L;
    private int responseStatus;

    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    public void throwIf(BooleanSupplier f) {
        throwIf(f.getAsBoolean());
    }

    public void throwIf(boolean b) {
        if (b) {
            throw this;
        }
    }
}
