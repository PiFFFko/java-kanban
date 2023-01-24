package ru.piven.tracker.service.exceptions;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(Throwable cause) {
        super(cause);
    }

    public ManagerSaveException() {
    }
}
