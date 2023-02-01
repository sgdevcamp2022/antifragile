package com.sgdevcamp.postservice.model;

import lombok.Data;

@Data
public class Image {
    private String id;

    private String originalFileName;

    private String serverFileName;

    private String path;

}
