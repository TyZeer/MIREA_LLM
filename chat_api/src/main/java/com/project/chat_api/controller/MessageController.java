package com.project.chat_api.controller;

import com.project.chat_api.models.Topic;
import com.project.chat_api.payload.request.MessageRequest;
import com.project.chat_api.payload.request.TopicRequest;
import com.project.chat_api.services.MessageService;
import com.project.chat_api.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/v1")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private TopicService topicService;

    @PostMapping("/messages/{sender_id}/{topic_id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> sendMessageAndReceiveAnswer(@PathVariable("sender_id") Long sender, @PathVariable("topic_id") Long topic_id, @RequestBody MessageRequest contents) throws Exception {
        try{
            return new ResponseEntity<>( messageService.sendAndReceiveMessage(sender, topic_id, contents), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/messages/{topic_id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> showAllMessagesByTopicId(@PathVariable Long topic_id){
        try{
            return new ResponseEntity<>(messageService.showMessagesByTopic(topic_id), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{user_id}/topics")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> showAllTopicsByUserId(@PathVariable Long user_id){
        try{
            return new ResponseEntity<>(topicService.showAllTopicsByUser(user_id), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/create/topic")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createNewTopic(@RequestBody TopicRequest topicRequest){
        try {
            return new ResponseEntity<>(topicService.createTopicByUser(topicRequest), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}

