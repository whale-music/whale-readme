package org.musicbox.exception;

public class SongListDoesNotExistException extends BaseException {
    public SongListDoesNotExistException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }
}
