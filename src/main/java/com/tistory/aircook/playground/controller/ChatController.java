package com.tistory.aircook.playground.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Tag(name = "Chat", description = "WebSocket 채팅 API")
@Controller
@Slf4j
public class ChatController {

    @Data
    public static class ChatMessage {
        private String sender;
        private String message;
        private LocalDateTime timestamp;
    }

    @Operation(summary = "채팅 메시지 전송", description = "클라이언트로부터 채팅 메시지를 수신하고 모든 구독자에게 브로드캐스트합니다.")
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        log.debug("Message from client(browser): {}", chatMessage);
        return chatMessage;
    }
}

