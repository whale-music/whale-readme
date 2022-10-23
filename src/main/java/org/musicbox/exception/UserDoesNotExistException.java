package org.musicbox.exception;

public class UserDoesNotExistException extends BaseException {
    public UserDoesNotExistException(String resultCode, String resultMsg) {
        super(resultCode, resultMsg);
    }
}
