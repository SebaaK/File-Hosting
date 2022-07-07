package kots.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileMetadataDto {

    private Long id;
    private String name;
    private String type;
}
