package com.sidequest.media.interfaces.dto;

import lombok.Data;

@Data
public class MediaRegisterRequest {
    private String fileName;
    private String fileKey;
    private String fileType; // image, video
    private String url;
}

