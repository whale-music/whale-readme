package org.common.exception;

import java.io.Serial;

/**
 * 异常类
 *
 * @author Sakura
 */
public class BaseException extends RuntimeException implements BaseErrorInfoInterface {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误码
     */
    private final String errorCode;
    /**
     * 错误信息
     */
    private final String errorMsg;
    
    public BaseException() {
        super();
        this.errorCode = null;
        this.errorMsg = null;
    }
    
    public BaseException(BaseErrorInfoInterface errorInfoInterface) {
        super(errorInfoInterface.getCode() + "," + errorInfoInterface.getResultMsg());
        this.errorCode = errorInfoInterface.getCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }
    
    public BaseException(BaseErrorInfoInterface errorInfoInterface, Throwable cause) {
        super(errorInfoInterface.getCode() + "," + errorInfoInterface.getResultMsg(), cause);
        this.errorCode = errorInfoInterface.getCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }
    
    public BaseException(String errorMsg) {
        super(errorMsg);
        this.errorCode = null;
        this.errorMsg = errorMsg;
    }
    
    public BaseException(String errorCode, String errorMsg) {
        super(errorCode + "," + errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    public BaseException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode + "," + errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    
    @Override
    public String getCode() {
        return errorCode;
    }
    
    
    @Override
    public String getResultMsg() {
        return errorMsg;
    }
    
}
