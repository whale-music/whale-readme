package org.musicbox.exception;

public class SongExistException extends BaseException {
    public SongExistException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }
}
