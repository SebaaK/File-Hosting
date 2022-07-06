package kots.service;

import kots.exception.CannotProcessedFileException;
import kots.exception.IncorrectFileTypeException;
import kots.exception.NoFileException;
import kots.model.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileValidator {

    @Value("#{'${app.acceptableExtensionFileList}'.split(',')}")
    private List<String> acceptableExtensionFile;

    public File validate(MultipartFile file) {
        checkFileIsAllRight(file);
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

    private void checkFileIsAllRight(MultipartFile file) {
        if(!fileExtensionIsAcceptable(checkFileIsExist(file).getContentType())) {
            String extensions = acceptableExtensionFile.stream()
                    .collect(Collectors.joining(", "));
            throw new IncorrectFileTypeException("Incorrect file type. Required: " + extensions);
        }
    }

    private boolean fileExtensionIsAcceptable(String contentType) {
        return acceptableExtensionFile.contains(contentType);
    }

    private MultipartFile checkFileIsExist(MultipartFile file) {
        if(!file.isEmpty())
            return file;
        else
            throw new NoFileException("File not found!");
    }
}
