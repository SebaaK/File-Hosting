package kots.service;

import kots.controller.dto.FileDownloadDto;
import kots.controller.dto.FileMetadataDto;
import kots.exception.NoFileException;
import kots.model.File;
import kots.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

import static kots.service.mapper.FileMapper.toFileMetaDataDto;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileValidator fileValidator;

    public FileMetadataDto store(MultipartFile file) {
        File validateFile = fileValidator.validate(file);
        return toFileMetaDataDto(fileRepository.save(validateFile));
    }

    public FileDownloadDto getFile(long id) {
        File file = getSingleFile(id);
        return FileDownloadDto.builder()
                .name(file.getName())
                .type(file.getType())
                .fileResource(new ByteArrayResource(file.getData()))
                .build();
    }

    private File getSingleFile(long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new NoFileException("File not exist!"));
    }

    public List<FileMetadataDto> getFiles() {
        return toFileMetaDataDto(fileRepository.findAll());
    }

    public void deleteFile(long id) {
        File file = getSingleFile(id);
        fileRepository.delete(file);
    }
}
