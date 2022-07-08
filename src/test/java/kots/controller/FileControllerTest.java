package kots.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kots.controller.dto.FileMetadataDto;
import kots.model.File;
import kots.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class FileControllerTest {

    public static final String URL_TEMPLATE = "/api/files";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FileRepository fileRepository;

    @Test
    void shouldSaveFileInDatabase() throws Exception {
        // given
        MockMultipartFile fileToSave = new MockMultipartFile(
                "file",
                "file.pdf",
                "application/pdf",
                "TestFile".getBytes());

        // when & then
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .multipart(URL_TEMPLATE)
                        .file(fileToSave)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        FileMetadataDto fileMetadataDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), FileMetadataDto.class);

        assertDoesNotThrow(() -> getFileFromDatabase(fileMetadataDto.getId()));
        File fileFromDatabase = getFileFromDatabase(fileMetadataDto.getId());
        assertThat(fileFromDatabase.getType()).isEqualTo(fileToSave.getContentType());
        assertThat(fileFromDatabase.getData()).isEqualTo(fileToSave.getBytes());
    }

    private File getFileFromDatabase(long id) {
        return fileRepository.findById(id).get();
    }
}