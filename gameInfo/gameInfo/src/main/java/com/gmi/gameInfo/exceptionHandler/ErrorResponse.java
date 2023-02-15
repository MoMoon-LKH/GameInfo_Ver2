package com.gmi.gameInfo.exceptionHandler;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "에러 응답")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @ApiModelProperty(value = "에러 내용")
    private String message;
    @ApiModelProperty(value = "에러 코드")
    private int status;

}
