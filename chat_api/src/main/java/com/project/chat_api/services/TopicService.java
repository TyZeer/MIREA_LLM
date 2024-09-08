package com.project.chat_api.services;

import com.project.chat_api.models.Topic;
import com.project.chat_api.payload.request.TopicRequest;
import com.project.chat_api.repos.MessageRepository;
import com.project.chat_api.repos.TopicRepository;
import com.project.chat_api.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static SecureRandom random = new SecureRandom();

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    public Topic createNewTopic(long sender_id){
        Topic newTopic = new Topic();
        newTopic = giveTopicName(newTopic);
        newTopic.user = userRepository.findUserById(sender_id);
        return newTopic;
    }
    public Topic giveTopicName(Topic topic){
        //call to api to set name
        //А похуй
        // Щас будет прикол

        topic.setTopicName(generateRandomString(10));
        return topic;
    }
    @Transactional
    public Topic createTopicByUser(TopicRequest topicRequest){
        Topic topic = Topic.builder()
                        .topicName(topicRequest.getTopicName())
                        .user(userRepository.findUserById(topicRequest.getUser_id()))
                        .build();
        topicRepository.save(topic);
        return topic;
    }
    public List<Topic> showAllTopicsByUser(Long user_id){

        return topicRepository.findAllByUserId(user_id);
    }
}
