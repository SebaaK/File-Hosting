package kots.service;

import kots.exception.IncorrectFileTypeException;
import kots.exception.NoFileException;
import kots.model.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static kots.service.mapper.FileMapper.toFile;

@Component
public class FileValidator {

    @Value("#{'${files.acceptableExtensionFileList}'.split(',')}")
    private List<String> acceptableExtensionFile;

    public File validate(MultipartFile file) {
        if(!isSupportedExtension(checkIfFileExists(file).getContentType())) {
            String extensions = acceptableExtensionFile.stream()
                    .collect(Collectors.joining(", "));
            throw new IncorrectFileTypeException("Incorrect file type. Required: " + extensions);
        }
        return toFile(file);
    }

    private boolean isSupportedExtension(String contentType) {
        return acceptableExtensionFile.contains(contentType);
    }

    private MultipartFile checkIfFileExists(MultipartFile file) {
        if(!file.isEmpty())
            return file;
        else
            throw new NoFileException("File not found!");
    }
}
