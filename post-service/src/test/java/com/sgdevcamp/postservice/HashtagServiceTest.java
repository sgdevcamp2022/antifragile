package com.sgdevcamp.postservice;

import com.sgdevcamp.postservice.model.Hashtag;
import com.sgdevcamp.postservice.repository.HashtagRepository;
import com.sgdevcamp.postservice.service.HashtagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
        hashtag = Hashtag.builder()
                .tag("#맞팔")
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

}


