package com.sgdevcamp.membershipservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    private String original_file_name;

    private String server_file_name;

    private String path;

    private Long size;

    private LocalDateTime create_at;

    private LocalDateTime update_at;

}
