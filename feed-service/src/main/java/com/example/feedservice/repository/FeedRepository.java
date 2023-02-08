package com.example.feedservice.repository;

import com.example.feedservice.model.UserFeed;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Slice;

public interface FeedRepository extends CassandraRepository<UserFeed, String> {
    Slice<UserFeed> findByUsername(String username, CassandraPageRequest request);
}
