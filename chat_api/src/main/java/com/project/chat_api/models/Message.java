package com.project.chat_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "message")
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long senderId;
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    private String contents;
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topicId;
}
