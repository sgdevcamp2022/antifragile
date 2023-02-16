package com.sgdevcamp.membershipservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImageRequest {

    private String original_file_name;

    private String server_file_name;

    private String path;

    private Long size;

    private Date create_at;

    private Date update_at;
}
