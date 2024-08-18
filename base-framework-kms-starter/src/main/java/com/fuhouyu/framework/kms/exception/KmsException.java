package com.fuhouyu.framework.kms.exception;

/**
 * <p>
 * kms异常类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/1/9 09:29
 */
public class KmsException extends RuntimeException {

    public KmsException() {
    }

    public KmsException(String message) {
        super(message);
    }

    public KmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public KmsException(Throwable cause) {
        super(cause);
    }

    public KmsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
