package com.project.chat_api.services;

import com.project.chat_api.models.Message;
import com.project.chat_api.models.MessageType;
import com.project.chat_api.models.Topic;
import com.project.chat_api.payload.request.MessageRequest;
import com.project.chat_api.repos.MessageRepository;
import com.project.chat_api.repos.TopicRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MessageService {

    private final TopicRepository topicRepository;
    private final MessageRepository messageRepository;
    private final  TopicService topicService;
    @Transactional
    public String sendAndReceiveMessage(long sender, long topic_id, MessageRequest contents) throws Exception {
        Topic emptyTopic = new Topic();
        boolean flag = false;
        if (topicRepository.existsById(topic_id)) {
            Message message = Message.builder()
                    .senderId(sender)
                    .messageType(MessageType.USER_MESSAGE)
                    .timestamp(LocalDateTime.now())
                    .contents(contents.getText())
                    .topicId(topicRepository.findById(topic_id))
                    .build();
            messageRepository.save(message);
        } else {
            Topic topic = topicService.createNewTopic(sender);
            topic = topicService.giveTopicName(topic);
            topicRepository.save(topic);
            topic = topicRepository.findByTopicName(topic.topicName);
            var newTopicName = "Topic " + topic.id;
            topicRepository.updateTopicNameById(topic.id, newTopicName); //TODO: ВРОДЕ ВСЕ АХУЕННО ПОПРАВИЛ
            Message message = Message.builder()
                    .senderId(sender)
                    .messageType(MessageType.USER_MESSAGE)
                    .timestamp(LocalDateTime.now())
                    .contents(contents.getText())
                    .topicId(topic)
                    .build();
            messageRepository.save(message);
            emptyTopic = topic;
            flag = true;
        }

            OkHttpClient client = new OkHttpClient();
            contents.setText(contents.getText().replace("/n",""));
            String json = "{\"text\": \"" + contents.getText() + "\"}";
            json = json.replaceAll("[\n\r]", "");

            RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url("http://localhost:5000/retelling")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);
            String result = jsonObject.getString("result");
            Message receivedMessage = Message.builder()
                    .senderId(0L)
                    .messageType(MessageType.GPT_MESSAGE)
                    .topicId(topicRepository.findById(topic_id))
                    .timestamp(LocalDateTime.now())
                    .contents(result)
                    .build();
            messageRepository.save(receivedMessage);
            String returnedMessage = receivedMessage.getContents();
            byte[] Bugbytes = returnedMessage.getBytes(StandardCharsets.UTF_8);
            returnedMessage = new String(Bugbytes, StandardCharsets.UTF_8);
            return receivedMessage.getContents();

    }

    public List<Message> showMessagesByTopic(long topicId){
        List<Message> messages;
        if (topicRepository.existsById(topicId)){
            Topic topic = topicRepository.findById(topicId);
            messages = messageRepository.findAllByTopicIdOrderByTimestamp(topic, Sort.by(Sort.Direction.DESC, "timestamp"));
            for (Message message : messages){
                byte[] bytes = message.getContents().getBytes(StandardCharsets.UTF_8);
                message.setContents(new String(bytes, StandardCharsets.UTF_8));
            }
            return messages;
        }
        return null;
    }
}
