package kots.controller.mapper;

import kots.model.File;
import kots.controller.dto.FileDto;

public class FileMapper {

    public static FileDto mapToIdNameTypeDto(File file) {
        return FileDto.builder()
                .id(file.getId())
                .name(file.getName())
                .type(file.getType())
                .build();
    }

}
