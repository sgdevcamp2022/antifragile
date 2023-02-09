package com.sgdevcamp.postservice.service;

import com.sgdevcamp.postservice.exception.CustomException;
import com.sgdevcamp.postservice.model.Hashtag;
import com.sgdevcamp.postservice.model.Post;
import com.sgdevcamp.postservice.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.sgdevcamp.postservice.exception.CustomExceptionStatus.NOT_FOUND_HASHTAG;

@Service
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public void createHashtags(List<String> hashtags, Post post){
        for(int i=0; i<hashtags.size(); i++){
            String tag_name = hashtags.get(i);
            Hashtag hashtag = hashtagRepository.findByTag(tag_name).get();

            if(hashtag != null){
                hashtag.setCount(hashtag.getCount() + 1);
            }else {
                ArrayList post_list = new ArrayList<>();
                post_list.add(post);

                Hashtag.builder()
                        .tag(tag_name)
                        .posts(new ArrayList<Post>(post_list))
                        .count(0L)
                        .build();
            }

            hashtagRepository.save(hashtag);
        }
    }

    public void deleteHashtags(List<String> hashtags){
        for(int i=0; i<hashtags.size(); i++){
            Hashtag hashtag = hashtagRepository.findByTag(hashtags.get(i)).get();

            if(hashtag == null) throw new CustomException(NOT_FOUND_HASHTAG);

            hashtag.setCount(hashtag.getCount() - 1);
            hashtagRepository.save(hashtag);
        }
    }
}
