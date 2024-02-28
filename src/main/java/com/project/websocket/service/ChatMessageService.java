package com.project.websocket.service;

import com.project.websocket.exeption.ResourceNotFoundException;
import com.project.websocket.model.ChatMessage;
import com.project.websocket.model.MessageStatus;
import com.project.websocket.repos.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final MongoOperations mongoOperations;

    public ChatMessage save(ChatMessage chatMessage)
    {
        var chatId = chatRoomService.getChatRoomId(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(),
                true
        ).orElse("Nothing was found");
        chatMessage.setChatId(chatId);
        repository.save(chatMessage);
        return chatMessage;
    }
    public List<ChatMessage> findChatMessages(
            String senderId, String recipientId
    )
    {
        var chatId = chatRoomService.getChatRoomId(senderId
        ,recipientId,false);

        Optional<List<ChatMessage>> messages = chatId.map(repository::findByChatId);
        if (messages.isPresent()){
            for (var message:
                    messages.get()) {
                updateStatuses(message.getSenderId(),
                        message.getRecipientId(),
                        MessageStatus.DELIVERED );
            }
        }

        return chatId.map(repository::findByChatId).orElse(
                new ArrayList<>()
        );
    }
    public ChatMessage findById(String id) {
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    public long countNewMessages(String senderId, String recipientId) {
        return repository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, MessageStatus.RECEIVED);
    }
    public void updateStatuses(String senderId, String recipientId, MessageStatus status) {
        Query query = new Query(
                Criteria
                        .where("senderId").is(senderId)
                        .and("recipientId").is(recipientId));
        Update update = Update.update("status", status);
        mongoOperations.updateMulti(query, update, ChatMessage.class);
    }
}

