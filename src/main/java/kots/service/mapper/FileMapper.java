package kots.service.mapper;

import kots.controller.dto.FileDto;
import kots.controller.dto.FileMetaDataDto;
import kots.exception.CannotProcessedFileException;
import kots.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class FileMapper {

    private FileMapper() {}

    public static FileDto toFileDto(File file) {
        return FileDto.builder()
                .id(file.getId())
                .name(file.getName())
                .type(file.getType())
                .data(file.getData())
                .build();
    }

    public static FileMetaDataDto toFileMetaDataDto(File file) {
        return FileMetaDataDto.builder()
                .id(file.getId())
                .name(file.getName())
                .type(file.getType())
                .build();
    }

    public static List<FileMetaDataDto> toFileMetaDataDto(List<File> fileList) {
        return fileList.stream()
                .map(FileMapper::toFileMetaDataDto)
                .toList();
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
