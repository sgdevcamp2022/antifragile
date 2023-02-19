package com.sgdevcamp.membershipservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sgdevcamp.membershipservice.model.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Transactional
    public Profile uploadMediaToS3(MultipartFile file, String username) throws IOException {

        Profile image;
        long now = (new Date()).getTime();

        String fileName = now + file.getOriginalFilename();
        log.info("File name: " + fileName);

        String contentType = FileUtil.findContentType(file.getContentType());
        log.info("Content Type change to Enum Type " + contentType);

        String folder = FileUtil.findFolder(fileName, username, contentType);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);

        try{
            amazonS3.putObject(
                    new PutObjectRequest(bucket, folder, file.getInputStream(), metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            image = Profile.builder()
                    .original_file_name(file.getOriginalFilename())
                    .path(amazonS3.getUrl(bucket, fileName).toString())
                    .size(file.getResource().contentLength())
                    .server_file_name(fileName)
                    .update_at(LocalDateTime.now())
                    .build();
            }catch (IOException e){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }

        log.info("Successfully upload file");

        return image;
    }

    public void deleteMediaToS3(String image){

        amazonS3.deleteObject(new DeleteObjectRequest(bucket, image));

        log.info("Successfully deleted upload file");
    }
}
