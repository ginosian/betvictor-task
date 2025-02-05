package com.task.betvictortask.controller.error;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class ErrorResponse {

    private final List<ApiError> errors;

    public ErrorResponse() {
        this.errors = new ArrayList<>();
    }

    public void addError(ApiError error) {
        this.errors.add(error);
    }
}
