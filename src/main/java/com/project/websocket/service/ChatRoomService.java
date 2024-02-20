package com.project.websocket.service;

import com.project.websocket.model.ChatRoom;
import com.project.websocket.repos.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatId(String senderId, String recipientId, boolean createIfNotExists){

        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (!createIfNotExists){
                        return Optional.empty();
                    }
                    var chatId =
                            String.format("%s_%s", senderId, recipientId);
                    ChatRoom sender = ChatRoom
                            .builder().senderId(senderId)
                            .recipientId(recipientId)
                            .chatId(chatId)
                            .build();
                    ChatRoom receiver = ChatRoom
                            .builder()
                            .senderId(recipientId)
                            .recipientId(senderId)
                            .chatId(chatId)
                            .build();
                    chatRoomRepository.save(sender);
                    chatRoomRepository.save(receiver);

                    return Optional.of(chatId);

                });
    }
}
