package com.sidequest.core.application;

import com.sidequest.core.infrastructure.PostDO;
import com.sidequest.core.infrastructure.mapper.PostMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoReadyConsumer {

    private final PostMapper postMapper;

    @KafkaListener(topics = "video-ready-topic", groupId = "core-group")
    public void onVideoReady(String mediaIdStr, String hlsUrl) {
        log.info("Received video ready event for mediaId: {}, HLS URL: {}", mediaIdStr, hlsUrl);
        try {
            Long mediaId = Long.parseLong(mediaIdStr);
            
            // 更新所有关联该 mediaId 的帖子视频地址为 HLS 地址
            postMapper.update(null, new LambdaUpdateWrapper<PostDO>()
                    .eq(PostDO::getMediaId, mediaId)
                    .set(PostDO::getVideoUrl, hlsUrl));
            
            log.info("Successfully updated post video URL to HLS for mediaId: {}", mediaId);
        } catch (Exception e) {
            log.error("Failed to update post video URL for mediaId: {}", mediaIdStr, e);
        }
    }
}

