package com.sgdevcamp.postservice.repository.feed;

import com.sgdevcamp.postservice.model.feed.UserFeed;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Slice;

public interface FeedRepository extends CassandraRepository<UserFeed, String> {
    Slice<UserFeed> findByUsername(String username, CassandraPageRequest request);
}
