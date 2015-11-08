package com.go.euro.test.exception;

public class DataReaderException extends RuntimeException {

    public DataReaderException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DataReaderException(String message)
    {
        super(message);
    }
}
