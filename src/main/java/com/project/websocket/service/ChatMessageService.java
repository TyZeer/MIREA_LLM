package com.project.websocket.service;

import com.project.websocket.exeption.ResourceNotFoundException;
import com.project.websocket.model.ChatMessage;
import com.project.websocket.model.MessageStatus;
import com.project.websocket.repos.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private MongoOperations mongoOperations;

    public ChatMessage save(ChatMessage chatMessage){
        chatMessage.setStatus(MessageStatus.RECEIVED);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }
    public long countNewMessages(String senderId, String receiverId){
        return chatMessageRepository
                .countBySenderIdAndRecipientIdAndStatus(senderId,receiverId,MessageStatus.RECEIVED);
    }
    public List<ChatMessage> findChatMessages(String senderId, String receiverId){
        var chatId = chatRoomService.getChatId(senderId, receiverId, false);
        var messages = chatId.map(id -> chatMessageRepository.findByChatId(id))
                .orElse(new ArrayList<>());
        if (!messages.isEmpty()){
            updateStatuses(senderId,receiverId);
        }
        return messages;
    }

    private void updateStatuses(String senderId, String receiverId) {
        Query query = new Query(
                Criteria
                        .where("senderId").is(senderId)
                        .and("receiverId").is(receiverId)
        );
        Update update = Update.update("status", MessageStatus.DELIVERED);
        mongoOperations.updateMulti(query,update,ChatMessage.class);
    }
    public ChatMessage findById(String id){
        return chatMessageRepository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return chatMessageRepository.save(chatMessage);
                }).orElseThrow(() ->
                        new ResourceNotFoundException("message with id = "+ id+" was not found"));
    }

}
