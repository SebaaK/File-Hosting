package kots.service;

import kots.controller.dto.FileDto;
import kots.controller.dto.FileMetaDataDto;
import kots.exception.NoFileException;
import kots.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static kots.service.mapper.FileMapper.toFileDto;
import static kots.service.mapper.FileMapper.toFileMetaDataDto;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileValidator fileValidator;

    public FileMetaDataDto store(MultipartFile file) {
        return toFileMetaDataDto(fileRepository.save(fileValidator.validate(file)));
    }

    public FileDto getFile(long id) {
        return toFileDto(fileRepository.findById(id)
                .orElseThrow(() -> new NoFileException("File not exist!")));
    }

    public List<FileMetaDataDto> getAllFiles() {
        return toFileMetaDataDto(fileRepository.findAll());
    }
}
