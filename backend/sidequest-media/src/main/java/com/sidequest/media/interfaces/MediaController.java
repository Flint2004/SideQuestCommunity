package com.sidequest.media.interfaces;

import com.sidequest.common.Result;
import com.sidequest.media.application.MediaService;
import com.sidequest.media.domain.Danmaku;
import com.sidequest.media.infrastructure.MediaDO;
import com.sidequest.media.interfaces.dto.DanmakuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;
    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/list")
    public Result<List<MediaDO>> getMyMedia(@RequestParam Long authorId) {
        return Result.success(mediaService.getAuthorMedia(authorId));
    }

    @GetMapping("/upload-url")
    public Result<String> getUploadUrl(@RequestParam String fileName) {
        return Result.success(mediaService.getUploadUrl(fileName));
    }

    @GetMapping("/status/{id}")
    public Result<Integer> getStatus(@PathVariable Long id) {
        return Result.success(mediaService.getStatus(id));
    }

    @PostMapping("/danmaku")
    public Result<String> sendDanmaku(@RequestBody Danmaku danmaku) {
        // 使用 Redis ZSet 存储弹幕，Score 为视频偏移时间
        String key = "danmaku:" + danmaku.getVideoId();
        redisTemplate.opsForZSet().add(key, danmaku, danmaku.getTimeOffsetMs());
        return Result.success("Danmaku sent");
    }

    @GetMapping("/danmaku")
    public Result<List<DanmakuVO>> getDanmaku(@RequestParam Long videoId, @RequestParam Long fromMs, @RequestParam Long toMs) {
        String key = "danmaku:" + videoId;
        List<Object> range = List.copyOf(redisTemplate.opsForZSet().rangeByScore(key, fromMs, toMs));
        List<DanmakuVO> vos = range.stream().map(obj -> {
            Danmaku danmaku = (Danmaku) obj;
            DanmakuVO vo = new DanmakuVO();
            BeanUtils.copyProperties(danmaku, vo);
            return vo;
        }).collect(Collectors.toList());
        return Result.success(vos);
    }
}

