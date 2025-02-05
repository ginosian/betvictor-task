package com.task.betvictortask.controller.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodes {
    RV1("Failed to convert '%s'. Invalid value for property:'%s'. Valid values are: %s"),
    RV2("Invalid 'p' value. Range for 'p' is from 1 to 10, both inclusive.");

    private final String message;
}
