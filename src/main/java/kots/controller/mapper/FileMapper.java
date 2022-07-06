package kots.controller.mapper;

import kots.exception.CannotProcessedFileException;
import kots.model.File;
import kots.controller.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileMapper {

    public static FileDto toFileDto(File file) {
        return FileDto.builder()
                .id(file.getId())
                .name(file.getName())
                .type(file.getType())
                .build();
    }

    public static File toFile(MultipartFile file) {
        try {
            return File.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .data(file.getBytes())
                    .build();
        } catch (IOException e) {
            throw new CannotProcessedFileException("Cannot processed this file");
        }
    }
}
