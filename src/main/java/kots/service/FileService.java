package kots.service;

import kots.controller.dto.FileDto;
import kots.repository.FileRepository;
import kots.service.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileValidator fileValidator;

    public FileDto store(MultipartFile file) {
        return FileMapper.toFileDto(fileRepository.save(fileValidator.validate(file)));
    }
}
