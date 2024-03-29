package com.project.websocket.repos;

import com.project.websocket.model.ChatMessage;
import com.project.websocket.model.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {


    List<ChatMessage> findByChatId(String chatId);

    long countBySenderIdAndRecipientIdAndStatus(String senderId, String recipientId, MessageStatus messageStatus);
}
