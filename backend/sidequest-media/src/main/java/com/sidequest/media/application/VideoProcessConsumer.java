package com.sidequest.media.application;

import com.sidequest.media.infrastructure.MediaDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoProcessConsumer {

    private final MediaService mediaService;

    @KafkaListener(topics = "video-process-topic", groupId = "media-group")
    public void onVideoProcess(@Header(KafkaHeaders.RECEIVED_KEY) String mediaIdStr, @Payload String message) {
        log.info("Received video processing task. mediaId: {}, message: {}", mediaIdStr, message);
        try {
            Long mediaId = Long.parseLong(mediaIdStr);
            
            // 调用真正的 HLS 处理逻辑
            mediaService.processHls(mediaId);
            
        } catch (Exception e) {
            log.error("Error processing video task for mediaId: {}", mediaIdStr, e);
            // 失败更新状态
            try {
                Long mediaId = Long.parseLong(mediaIdStr);
                mediaService.updateStatus(mediaId, MediaDO.STATUS_FAILED);
            } catch (Exception ignored) {}
        }
    }
}

