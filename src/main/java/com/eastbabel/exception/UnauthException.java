package com.eastbabel.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnauthException extends com.eastbabel.exception.CustomException {
    private static final long serialVersionUID = 4171083212324621309L;

    public UnauthException(String message) {
        super(message);
    }

    public UnauthException(String message, Throwable e) {
        super(message, e);
    }
}
