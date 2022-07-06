package kots.service;

import kots.model.File;
import kots.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileValidator fileValidator;

    public File store(MultipartFile file) {
        return fileRepository.save(fileValidator.validate(file));
    }
}
