package kots.controller.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.Resource;

@Getter
@Builder
public class FileDownloadDto {

    private String name;
    private String type;
    private Resource fileResource;
}
