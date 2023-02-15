package com.sgdevcamp.postservice;

import com.sgdevcamp.postservice.model.Hashtag;
import com.sgdevcamp.postservice.model.Image;
import com.sgdevcamp.postservice.model.Post;
import com.sgdevcamp.postservice.repository.HashtagRepository;
import com.sgdevcamp.postservice.service.HashtagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HashtagServiceTest {

    @InjectMocks
    private HashtagService hashtagService;

    @Mock
    private HashtagRepository hashtagRepository;

    private static Hashtag hashtag;

    @BeforeEach
    public void beforeEach(){
        Post post = Post.builder()
                .id("1")
                .images(Arrays.asList(new Image()))
                .content("좋아요 눌러줘")
                .build();

        hashtag = Hashtag.builder()
                .tag("#맞팔")
                .postId(post)
                .build();
    }

    @Test
    @DisplayName("포스트의 해시태그 조회")
    public void getHashtagsInPost() {
        // given
        when(hashtagRepository.findByPostId(any())).thenReturn(Optional.of(hashtag));

        // when
        Optional<Hashtag> hashtag = hashtagService.getHashtag(any());

        // then
        assertThat(hashtag).isEqualTo(hashtag);
    }

    @Test
    @DisplayName("해시태그 검색")
    public void searchHashtag() {
        // given
        List<Hashtag> hashtagList = new ArrayList<>();
        hashtagList.add(hashtag);

        Page<Hashtag> hashtags = new PageImpl<>(hashtagList);

        when(hashtagRepository.findByTagContaining(any(), any())).thenReturn(hashtags);

        // when, then
        assertThat(hashtagService.searchHashtag(any(), any())).isNotEmpty();
    }

}


