package me.silvernine.tutorial.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorDto {
    private final int status;
    private final String message;
    private final List<FieldErrorInfo> fieldErrors;

    public ErrorDto(int status, String message) {
        this.status = status;
        this.message = message;
        this.fieldErrors = new ArrayList<>();
    }

    public void addFieldError(String objectName, String field, String message) {
        FieldErrorInfo error = new FieldErrorInfo(objectName, field, message);
        fieldErrors.add(error);
    }

    @Getter
    public static class FieldErrorInfo {
        private final String objectName;
        private final String field;
        private final String message;

        public FieldErrorInfo(String objectName, String field, String message) {
            this.objectName = objectName;
            this.field = field;
            this.message = message;
        }

    }
}
