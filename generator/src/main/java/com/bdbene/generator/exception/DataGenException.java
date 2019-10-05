package com.bdbene.generator.exception;

public class DataGenException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public DataGenException(Exception e) {
        super(e);
    }
    
    public DataGenException(String msg, Exception e) {
        super(msg, e);
    }
}