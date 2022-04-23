package com.together.levelup.dto;
import com.together.levelup.domain.post.PostCategory;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PostRequest {

    @NotNull
    private String title;

    @NotNull
    private String writer;

    @NotNull
    private String content;

    @NotNull
    private PostCategory category;

}
