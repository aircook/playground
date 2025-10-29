package com.tistory.aircook.playground.controller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@Slf4j
public class ChatController {

    @Data
    public static class ChatMessage {
        private String sender;
        private String message;
        private LocalDateTime timestamp;
    }

    // 클라이언트에서 /app/chat으로 보낸 메시지를 처리
    @MessageMapping("/chat")
    // 처리 후 /topic/messages로 브로드캐스트
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        // 서버에서 수신 시 timestamp 설정
        chatMessage.setTimestamp(LocalDateTime.now());
        log.debug("Message from client(browser): {}", chatMessage);
        // 클라이언트로 전송
        return chatMessage;
    }
}
