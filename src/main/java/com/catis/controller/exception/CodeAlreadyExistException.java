package com.catis.controller.exception;

public class CodeAlreadyExistException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CodeAlreadyExistException() {
        super();
    }

    public CodeAlreadyExistException(String s) {
        super(s);
    }
}
