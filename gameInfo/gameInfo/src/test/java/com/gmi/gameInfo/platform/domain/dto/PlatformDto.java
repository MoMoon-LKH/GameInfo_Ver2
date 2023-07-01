package com.gmi.gameInfo.platform.domain.dto;

import com.gmi.gameInfo.platform.domain.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlatformDto {

    private Long id;

    private String name;

    public PlatformDto(Platform platform) {
        this.id = platform.getId();
        this.name = platform.getName();
    }
}
