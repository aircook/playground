package com.tistory.aircook.playground.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP 기반 WebSocket 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 구독할 브로커 경로 (topic)
        registry.enableSimpleBroker("/topic");
        // 클라이언트가 메시지를 보낼 때 사용할 prefix (/app)
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 연결할 엔드포인트 설정
        registry.addEndpoint("/ws") // 웹소켓 연결 주소
                .setAllowedOriginPatterns("*") // 모든 도메인 허용 (CORS)
                .withSockJS(); // SockJS fallback 지원
    }
}
