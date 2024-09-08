package com.project.chat_api.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicRequest {
    @NotBlank
    //@Size(min = 3, max = 20)
    private long user_id;

    @NotBlank
    @Size(max = 50)
    @Email
    private String topicName;

}
