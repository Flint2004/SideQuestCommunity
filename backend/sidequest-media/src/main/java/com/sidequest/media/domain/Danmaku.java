package com.sidequest.media.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Danmaku implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long videoId;
    private Long userId;
    private String content;
    private Long timeOffsetMs;
    private String color;
    private LocalDateTime createTime;
}

