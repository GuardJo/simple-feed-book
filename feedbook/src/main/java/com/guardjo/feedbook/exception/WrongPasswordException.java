package com.guardjo.feedbook.exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("비밀번호가 올바르지 않습니다.");
    }
}
