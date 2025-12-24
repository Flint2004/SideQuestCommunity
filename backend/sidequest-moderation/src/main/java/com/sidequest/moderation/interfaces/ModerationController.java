package com.sidequest.moderation.interfaces;

import com.sidequest.common.Result;
import com.sidequest.moderation.application.ModerationService;
import com.sidequest.moderation.interfaces.dto.CheckRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/moderation")
@RequiredArgsConstructor
public class ModerationController {
    private final ModerationService moderationService;

    @PostMapping("/check")
    public Result<Boolean> checkText(@Valid @RequestBody CheckRequest request) {
        return Result.success(moderationService.checkText(request.getContent()));
    }
}

