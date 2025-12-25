package com.sidequest.search.interfaces;

import com.sidequest.common.Result;
import com.sidequest.search.domain.PostRepository;
import com.sidequest.search.infrastructure.PostDoc;
import com.sidequest.search.infrastructure.feign.IdentityClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final PostRepository postRepository;
    private final IdentityClient identityClient;

    @GetMapping("/posts")
    public Result<Page<PostDoc>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        // 修复搜索逻辑：仅对标题和标签进行搜索，且必须匹配其中之一
        Page<PostDoc> postDocs = postRepository.findByKeyword(keyword, keyword, pageable);
        postDocs.forEach(this::enrichAuthorInfo);
        return Result.success(postDocs);
    }

    @GetMapping("/user/posts")
    public Result<Page<PostDoc>> searchUserPosts(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<PostDoc> postDocs = postRepository.findByAuthorIdAndStatus(userId, 1, pageable);
        postDocs.forEach(this::enrichAuthorInfo);
        return Result.success(postDocs);
    }

    private void enrichAuthorInfo(PostDoc doc) {
        if (doc.getAuthorId() != null) {
            try {
                Result<IdentityClient.UserDTO> userRes = identityClient.getUserById(doc.getAuthorId());
                if (userRes.getCode() == 200 && userRes.getData() != null) {
                    doc.setAuthorName(userRes.getData().getNickname());
                    doc.setAuthorAvatar(userRes.getData().getAvatar());
                }
            } catch (Exception ignored) {
                // 如果身份服务不可用，保留原始数据
            }
        }
    }
}

