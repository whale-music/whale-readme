package org.musicbox.exception;

public class SongNotExistException extends BaseException {
    public SongNotExistException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }
}
