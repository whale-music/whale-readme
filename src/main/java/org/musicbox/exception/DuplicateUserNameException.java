package org.musicbox.exception;

public class DuplicateUserNameException extends BaseException {
    public DuplicateUserNameException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }
}
