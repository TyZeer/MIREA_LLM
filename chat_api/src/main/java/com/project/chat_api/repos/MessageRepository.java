package com.project.chat_api.repos;

import com.project.chat_api.models.Message;
import com.project.chat_api.models.Topic;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByTopicIdOrderByTimestamp(Topic topic, Sort sort);
}