package com.sgdevcamp.postservice.repository.follow;

import com.sgdevcamp.postservice.model.follow.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("follow-UserRepository")
public interface UserRepository extends Neo4jRepository<User, Long> {

    Optional<User> findByUserId(String userId);
    Optional<User> findByUsername(String username);
    List<User> findByUsernameIn(List<String> usernames);

    @Query("MATCH (n)-[r:IS_FOLLOWING]->() where n.username = $username RETURN COUNT(r)")
    Long findOutDegree(@Param("username") String username);

    @Query("MATCH (n)<-[r:IS_FOLLOWING]-() WHERE n.username = $username RETURN COUNT(r)")
    Long findInDegree(@Param("username") String username);

    @Query("MATCH (n1:User{ username : $userA }), (n2:User{username : $userB }) RETURN EXISTS((n1)-[:IS_FOLLOWING] ->(n2))")
    boolean isFollowing(@Param("userA") String userA, @Param("userB") String userB);

    @Query("MATCH (n:User{username : $username})<--(f:User) Return f")
    List<User> findFollowers(@Param("username") String username);

    @Query(value = "MATCH (n:User{username : $username})<--(f:User) Return f",
            countQuery = "MATCH (n:User{username : $username})<--(f:User) Return count(f)")
    Page<User> findFollowers(@Param("username") String username, Pageable pageable);

    @Query("MATCH (n:User{username : $username})-->(f:User) Return f")
    List<User> findFollowing(@Param("username") String username);

    @Query("MATCH (n)-[r:IS_FOLLOWING]->(m) WHERE n.username = $username1 AND m.username = $username2 DELETE r")
    void deleteFollowing(@Param("username1") String username1, @Param("username2") String username2);
}
