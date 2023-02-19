package com.sgdevcamp.postservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sgdevcamp.postservice.model.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Transactional
    public List<Image> uploadMediaToS3(List<MultipartFile> multipartFile, String username) throws IOException {

        List<Image> images = new LinkedList<>();
        long now = (new Date()).getTime();

        multipartFile.forEach(file -> {

            String fileName = now + file.getOriginalFilename();
            log.info("File name: " + fileName);

            String contentType = FileUtil.findContentType(file.getContentType());
            log.info("Content Type change to Enum Type " + contentType);

            String folder = FileUtil.findFolder(fileName, username, contentType);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            try{
                amazonS3.putObject(
                    new PutObjectRequest(bucket, folder, file.getInputStream(),
                            metadata).withCannedAcl(
                            CannedAccessControlList.PublicRead));

                images.add(Image.builder().path(amazonS3.getUrl(bucket, fileName).toString()).build());
            }catch (IOException e){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }
        });

        log.info("Successfully upload file");

        return images;
    }

    public void deleteMediaToS3(List<String> images){
        for (String image : images) {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, image));
        }

        log.info("Successfully deleted upload file");
    }
}
