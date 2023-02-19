package com.sgdevcamp.membershipservice.service;

import com.sgdevcamp.membershipservice.exception.CustomException;
import lombok.extern.slf4j.Slf4j;

import static com.sgdevcamp.membershipservice.exception.CustomExceptionStatus.FILE_CONVERT_FAIL;


@Slf4j
public class FileUtil {
    public static String findContentType(String contentType) {

        String[] mediaContentType = contentType.split("/");

        if (mediaContentType.length <= 0)
            throw new CustomException(FILE_CONVERT_FAIL);

        if (!(mediaContentType[0].toUpperCase().equals("IMAGE") || mediaContentType[0].toUpperCase().equals("VIDEO")))
            throw new CustomException(FILE_CONVERT_FAIL);

        return mediaContentType[0].toUpperCase();
    }

    public static String findFolder(String filename, String userName, String contentType) {

        String folder = "";
        folder += contentType + "/" + userName + "/" + contentType + "/" + filename;

        log.info("folder Name : " + folder);

        return folder;
    }

    public static String findExt(String fileName) {
        return fileName.split("\\.")[1];
    }
}