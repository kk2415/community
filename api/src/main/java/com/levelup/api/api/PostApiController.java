package com.levelup.api.api;

import com.levelup.api.service.FileService;
import com.levelup.api.service.MemberService;
import com.levelup.api.service.PostService;
import com.levelup.core.domain.file.FileStore;
import com.levelup.core.domain.file.ImageType;
import com.levelup.core.domain.file.UploadFile;
import com.levelup.core.domain.member.Member;
import com.levelup.core.domain.post.Post;
import com.levelup.core.dto.Result;
import com.levelup.core.dto.post.*;

import com.levelup.core.exception.PostNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "게시글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostApiController {

    private final PostService postService;
    private final MemberService memberService;
    private final FileStore fileStore;
    private final FileService fileService;

    /**
     * 생성
     * */
    @PostMapping("/post")
    public ResponseEntity create(@Validated @RequestBody CreatePostRequest postRequest,
                                 @AuthenticationPrincipal Long id) {
        Long postId = postService.create(id, postRequest.getChannelId(), postRequest.getTitle(),
                postRequest.getContent(), postRequest.getCategory());
        Post findPost = postService.findById(postId);

//        fileService.create(findPost, postRequest.getUploadFiles());

        PostResponse postResponse = new PostResponse(
                findPost.getId(),
                findPost.getMember().getId(),
                findPost.getTitle(),
                findPost.getWriter(),
                findPost.getContent(),
                DateTimeFormatter.ofPattern(DateFormat.DATE_FORMAT).format(findPost.getDateCreated()),
                findPost.getVoteCount(),
                findPost.getViews(),
                findPost.getComments().size(),
                findPost.getPostCategory()
        );

        EntityModel<PostResponse> model = EntityModel.of(postResponse)
                .add(linkTo(methodOn(this.getClass()).readPost(postId, "false")).withSelfRel());

        return new ResponseEntity(model, HttpStatus.CREATED);
    }

    @PostMapping("/post/files")
    public ResponseEntity storeFiles(MultipartFile file) throws IOException {
        UploadFile uploadFiles = fileStore.storeFile(ImageType.POST, file);

        if (uploadFiles == null) {
            return new ResponseEntity("파일이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        String storeFileName = uploadFiles.getStoreFileName();
        return new ResponseEntity(storeFileName, HttpStatus.OK);
    }


    /**
     * 조회
     * */
    @GetMapping("/posts")
    public Result findAllPost() {
        List<Post> findPosts = postService.findAll();

        List<PostResponse> postResponses = findPosts.stream()
                .map(p -> new PostResponse(p.getId(), p.getMember().getId(), p.getTitle(), p.getWriter(),
                        p.getContent(), DateTimeFormatter.ofPattern(DateFormat.DATE_FORMAT).format(p.getDateCreated()),
                        p.getVoteCount(), p.getViews(), p.getComments().size(), p.getPostCategory()))
                .collect(Collectors.toList());

        return new Result<>(postResponses, postResponses.size());
    }

    @GetMapping("/{channelId}/search/count")
    public int findAllPostWithQuery(@PathVariable Long channelId,
                                       @RequestParam(required = false) String field,
                                       @RequestParam(required = false) String query) {
        PostSearch postSearch = null;
        if (field != null && query != null) {
            postSearch = new PostSearch(field, query);
        }
        List<Post> findPosts = postService.findByChannelId(channelId, postSearch);

        return findPosts.size();
    }

    @GetMapping("/post/{postId}")
    public PostResponse readPost(@PathVariable Long postId,
                                 @RequestParam(required = false, defaultValue = "false") String view) {
        Post findPost = postService.findById(postId);

        if (view.equals("true")) {
            postService.addViews(findPost);
        }

        return new PostResponse(findPost.getId(), findPost.getMember().getId(), findPost.getTitle(), findPost.getWriter(),
                findPost.getContent(), DateTimeFormatter.ofPattern(DateFormat.DATE_FORMAT).format(findPost.getDateCreated()),
                findPost.getVoteCount(), findPost.getViews(),
                (int) findPost.getComments().stream().filter(c -> c.getParent() == null).count(), findPost.getPostCategory());
    }

    @GetMapping("/{channelId}/posts/{page}")
    public Result listingChannelPosts(@PathVariable Long channelId,
                                      @PathVariable int page,
                                      @RequestParam(required = false, defaultValue = "10") int postCount,
                                      @RequestParam(required = false) String field,
                                      @RequestParam(required = false) String query) {
        PostSearch postSearch = new PostSearch(field, query);

        List<Post> findPosts = postService.findByChannelId(channelId, page, postCount, postSearch);

        List<PostResponse> postResponses = findPosts.stream()
                .map(p -> new PostResponse(p.getId(), p.getMember().getId(), p.getTitle(), p.getWriter(), p.getContent(),
                        DateTimeFormatter.ofPattern(DateFormat.DATE_FORMAT).format(p.getDateCreated()), p.getVoteCount(),
                        p.getViews(), (int) p.getComments().stream().filter(c -> c.getParent() == null).count(),
                        p.getPostCategory()))
                .collect(Collectors.toList());

        return new Result(postResponses, postResponses.size());
    }

    @GetMapping("/post/{postId}/nextPost")
    public PostResponse findNextPost(@PathVariable Long postId) {
        Post findPost = postService.findNextPage(postId);

        if (findPost == null) {
            throw new PostNotFoundException();
        }

        return new PostResponse(findPost.getId(), findPost.getMember().getId(), findPost.getTitle(), findPost.getWriter(),
                findPost.getContent(), DateTimeFormatter.ofPattern(DateFormat.DATE_FORMAT).format(findPost.getDateCreated()),
                findPost.getVoteCount(), findPost.getViews(),
                (int) findPost.getComments().stream().filter(c -> c.getParent() == null).count(), findPost.getPostCategory());
    }

    @GetMapping("/post/{postId}/prevPost")
    public PostResponse findPrevPost(@PathVariable Long postId) {
        Post findPost = postService.findPrevPage(postId);

        if (findPost == null) {
            throw new PostNotFoundException();
        }

        return new PostResponse(findPost.getId(), findPost.getMember().getId(), findPost.getTitle(), findPost.getWriter(),
                findPost.getContent(), DateTimeFormatter.ofPattern(DateFormat.DATE_FORMAT).format(findPost.getDateCreated()),
                findPost.getVoteCount(), findPost.getViews(),
                (int) findPost.getComments().stream().filter(c -> c.getParent() == null).count(), findPost.getPostCategory());
    }

    @GetMapping("/post/{postId}/check-member")
    public ResponseEntity findPostByMemberId(@PathVariable Long postId, @RequestParam String email) {
        postService.oauth(postId, email);

        return new ResponseEntity(new Result("인증 성공", 1), HttpStatus.OK);
    }


    /**
     * 수정
     * */
    @PatchMapping("/post/{postId}")
    public UpdatePostResponse updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest postRequest,
                                         @AuthenticationPrincipal Long id) {
        System.out.println(postRequest.getContent());
        System.out.println(postRequest.getTitle());

        Member findMember = memberService.findOne(id);

        postService.updatePost(postId, findMember.getId(), postRequest.getTitle(),
                postRequest.getContent(), postRequest.getCategory());
        Post findPost = postService.findById(postId);

        return new UpdatePostResponse(findPost.getTitle(), findPost.getWriter(), findPost.getContent(), findPost.getPostCategory());
    }


    /**
     * 삭제
     * */
    @DeleteMapping("/post/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);

        return new ResponseEntity(new Result("게시물이 성공적으로 삭제되었습니다.", 1), HttpStatus.OK);
    }

}
