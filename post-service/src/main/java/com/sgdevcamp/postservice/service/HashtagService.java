package com.sgdevcamp.postservice.service;

import com.sgdevcamp.postservice.model.Hashtag;
import com.sgdevcamp.postservice.model.Post;
import com.sgdevcamp.postservice.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public void createHashtags(List<String> hashtags, Post post){

        List<Hashtag> hashtagList = hashtags.stream().map(t -> Hashtag.builder()
                .tag(t)
                .postId(post)
                .build()).collect(Collectors.toList());

        hashtagRepository.saveAll(hashtagList);
    }

    public Optional<Hashtag> getHashtag(String post_id){
        return hashtagRepository.findByPostId(post_id);
    }

    public void deleteHashtags(String post_id){
        if(hashtagRepository.findByPostId(post_id).isPresent()) hashtagRepository.deleteAllInBatchByPostId(post_id);
    }
}
