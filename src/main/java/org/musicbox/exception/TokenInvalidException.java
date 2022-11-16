package org.musicbox.exception;

public class TokenInvalidException extends BaseException {
    public TokenInvalidException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }
}
