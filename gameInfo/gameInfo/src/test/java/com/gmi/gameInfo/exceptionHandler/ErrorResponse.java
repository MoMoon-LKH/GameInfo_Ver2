package com.gmi.gameInfo.exceptionHandler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private String message;
    private int status;
}
