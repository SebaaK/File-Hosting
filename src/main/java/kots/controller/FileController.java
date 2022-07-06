package kots.controller;

import kots.controller.dto.FileDto;
import kots.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

import static kots.controller.mapper.FileMapper.mapToIdNameTypeDto;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<FileDto> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if(!file.isEmpty())
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToIdNameTypeDto(fileService.store(file)));
        else
            throw new FileNotFoundException("File not found!");
    }
}
