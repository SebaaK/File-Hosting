package kots.service;

import kots.model.File;
import kots.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private static List<String> acceptableExtensionFile = List.of("application/pdf");

    private final FileRepository fileRepository;

    public File store(MultipartFile file) throws IOException {
        if(fileExtensionIsAcceptable(file.getContentType())) {
            return fileRepository.save(File.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .data(file.getBytes())
                    .build());
        } else
            throw new IllegalArgumentException("Incorrect file type. PDF required.");
    }

    private boolean fileExtensionIsAcceptable(String contentType) {
        return acceptableExtensionFile.contains(contentType);
    }
}
