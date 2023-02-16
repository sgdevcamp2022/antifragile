package com.sgdevcamp.postservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    private String id;

    private String originalFileName;

    private String serverFileName;

    private String path;

}
