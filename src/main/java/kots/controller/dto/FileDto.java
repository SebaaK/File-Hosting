package kots.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileDto {

    private Long id;
    private String name;
    private String type;
    private byte[] data;
}
