package com.sidequest.core.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CreatePostDTO {
    @NotBlank(message = "Title cannot be blank")
    private String title;
    
    @NotBlank(message = "Content cannot be blank")
    private String content;
    
    @NotNull(message = "Section ID is required")
    private Long sectionId;
    
    private List<String> tags;
    private List<String> imageUrls;
    private String videoUrl;
    private String videoCoverUrl;
    private Integer videoDuration;
    private Long mediaId;
}

