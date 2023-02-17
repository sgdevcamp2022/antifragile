package com.sgdevcamp.postservice.service;

import com.sgdevcamp.postservice.model.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    private final S3Service s3Service;

    @Transactional
    public List<Image> save(List<MultipartFile> multipartFiles, String username) throws IOException {
        return s3Service.uploadMediaToS3(multipartFiles, username);
    }

    public void delete(List<Image> images){
        s3Service.deleteMediaToS3(images.stream().map(Image::getPath).collect(Collectors.toList()));
    }
}
