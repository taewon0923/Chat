package aa.chat1.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
public class ChatMessage{
    private String time;
    private String content;
}