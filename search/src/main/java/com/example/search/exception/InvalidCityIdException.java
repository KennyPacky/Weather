package com.example.search.exception;

public class InvalidCityIdException extends IllegalArgumentException{
    public InvalidCityIdException() {
    }

    public InvalidCityIdException(String s) {
        super(s);
    }

    public InvalidCityIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCityIdException(Throwable cause) {
        super(cause);
    }
}
