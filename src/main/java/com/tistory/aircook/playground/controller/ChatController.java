package com.tistory.aircook.playground.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // 클라이언트에서 /app/chat으로 보낸 메시지를 처리
    @MessageMapping("/chat")
    // 처리 후 /topic/messages로 브로드캐스트
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        // 서버에서 받은 메시지를 그대로 클라이언트로 전송
        return "Server: " + message;
    }
}
