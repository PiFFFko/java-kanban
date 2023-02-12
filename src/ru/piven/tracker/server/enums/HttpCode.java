package ru.piven.tracker.server.enums;

public enum HttpCode {
    SUCCESS(200),
    CREATED(201),
    METHOD_NOT_ALLOWED(405),
    BAD_REQUEST(400),
    INTERNAL_SERVER_ERROR(500);


    private int code;

    HttpCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

