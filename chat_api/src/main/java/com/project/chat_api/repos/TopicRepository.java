package com.project.chat_api.repos;

import com.project.chat_api.models.Topic;
import com.project.chat_api.models.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic,Long > {

    boolean existsById(long topicId);

    List<Topic> findAllByUserId(long user_id);

    Topic findByTopicName(String name);

    Topic findById(long id);

    @Modifying
    @Transactional
    @Query("UPDATE Topic t SET t.topicName = :topicName WHERE t.id = :id")
    void updateTopicNameById(@Param("id") long id, @Param("topicName") String topicName);


}

