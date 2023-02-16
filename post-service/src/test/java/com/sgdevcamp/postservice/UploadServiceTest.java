package com.sgdevcamp.postservice;

import com.sgdevcamp.postservice.model.Image;
import com.sgdevcamp.postservice.service.S3Service;
import com.sgdevcamp.postservice.service.UploadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UploadServiceTest {

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private UploadService uploadService;

    @Test
    @DisplayName("이미지 업로드")
    public void save() throws IOException {
        // given
        String username = "test00";
        List<MultipartFile> multipartFiles = new ArrayList<>();
        List<Image> saved_images = new ArrayList<>();
        saved_images.add(Image.builder().path("test\test.jpg").build());

        when(s3Service.uploadMediaToS3(multipartFiles, username)).thenReturn(saved_images);

        // when
        List<Image> images = uploadService.save(multipartFiles, username);

        // then
        assertThat(images).isNotEmpty();
    }
}
