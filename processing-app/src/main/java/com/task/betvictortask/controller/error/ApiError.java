package com.task.betvictortask.controller.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiError {

    private String code;
    private String message;
}
