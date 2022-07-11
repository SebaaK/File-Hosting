package kots.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kots.controller.dto.FileMetadataDto;
import kots.model.File;
import kots.repository.FileRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    public static final String BASE_URL_FILE_ENDPOINT = "/api/files";
    public static final String MESSAGE_PATH_RESPONSE = "$.message";
    public static final String PDF_CONTENT_TYPE = "application/pdf";
    public static final int ARRAY_COUNT = 10;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FileRepository fileRepository;

    @AfterAll
    void cleanUp() {
        fileRepository.deleteAll();
    }

    @Test
    public void shouldSaveFileEntityInDatabase() throws Exception {
        // given
        MockMultipartFile fileToSave = generateMockFile("file.pdf", PDF_CONTENT_TYPE);

        // when & then
        MvcResult mvcResult = mockMvc.perform(multipart(BASE_URL_FILE_ENDPOINT).file(fileToSave))
                .andExpect(status().isCreated())
                .andReturn();

        FileMetadataDto fileMetadataDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), FileMetadataDto.class);

        assertDoesNotThrow(() -> getFileFromDatabase(fileMetadataDto.getId()));
        File fileFromDatabase = getFileFromDatabase(fileMetadataDto.getId());
        assertThat(fileFromDatabase.getType()).isEqualTo(fileToSave.getContentType());
        assertThat(fileFromDatabase.getData()).isEqualTo(fileToSave.getBytes());
    }

    @Test
    public void shouldReceiveNotFoundStatusWhenNoFileIfTryToCallToSaveFile() throws Exception {
        // given
        MockMultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);

        // when & then
        mockMvc.perform(multipart(BASE_URL_FILE_ENDPOINT).file(emptyFile))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(MESSAGE_PATH_RESPONSE, is("File not found!")));
    }

    @Test
    public void shouldReceiveNotAcceptableStatusResponseWhenFileExtensionIsNotSupported() throws Exception {
        // given
        MockMultipartFile badFile = generateMockFile("file.txt", "text/plain");

        // when & then
        mockMvc.perform(multipart(BASE_URL_FILE_ENDPOINT).file(badFile).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath(MESSAGE_PATH_RESPONSE, Matchers.startsWith("Incorrect file type. Required: ")));
    }

    @Test
    public void shouldGetFileToDownload() throws Exception {
        // given
        File saveFile = fileRepository.save(new File(null, "test.pdf", PDF_CONTENT_TYPE, "TestFile".getBytes()));

        // when & then
        MvcResult result = mockMvc.perform(get(BASE_URL_FILE_ENDPOINT + "/" + saveFile.getId()))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentType()).isEqualTo(PDF_CONTENT_TYPE);
        assertThat(result.getResponse().getContentAsByteArray()).isEqualTo(saveFile.getData());
    }

    @Test
    public void shouldReceiveNotFoundStatusResponseWhenGetNotExistIdFile() throws Exception {
        mockMvc.perform(get(BASE_URL_FILE_ENDPOINT + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(MESSAGE_PATH_RESPONSE, is("File not exist!")));
    }

    @Test
    public void shouldFetchFilesList() throws Exception {
        // given
        fileRepository.saveAll(getRandomFileList(ARRAY_COUNT));

        // when & then
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL_FILE_ENDPOINT))
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        FileMetadataDto[] fileMetadataDtoList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), FileMetadataDto[].class);
        assertThat(fileMetadataDtoList).hasSizeGreaterThanOrEqualTo(ARRAY_COUNT);
    }

    @Test
    public void shouldDeleteFileEntityFromDatabase() throws Exception {
        // given
        Iterable<File> files = fileRepository.saveAll(getRandomFileList(ARRAY_COUNT));
        Long idFirstSaveFile = getIdFileFromIterable(files);

        // when & then
        mockMvc.perform(delete(BASE_URL_FILE_ENDPOINT + "/" + idFirstSaveFile))
                .andExpect(status().isNoContent());
        assertFalse(fileRepository.existsById(idFirstSaveFile));
    }

    private MockMultipartFile generateMockFile(String originalFilename, String contentType) {
        return new MockMultipartFile(
                "file",
                originalFilename,
                contentType,
                "TestFile".getBytes());
    }

    private Long getIdFileFromIterable(Iterable<File> files) {
        return files.iterator().next().getId();
    }

    private List<File> getRandomFileList(int count) {
        List<File> fileList = new ArrayList<>();
        for(int i = 1; i <= count; i++) {
            fileList.add(File.builder()
                    .name("file" + i + ".pdf")
                    .type(PDF_CONTENT_TYPE)
                    .data("testFile".getBytes())
                    .build());
        }
        return fileList;
    }

    private File getFileFromDatabase(long id) {
        return fileRepository.findById(id).get();
    }
}