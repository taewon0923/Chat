package aa.chat1.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> list = new CopyOnWriteArrayList<>();
    private static Map<WebSocketSession,String> users = new ConcurrentHashMap<>();
    boolean mode = false;
    private static String sender;
    private static String user;
    private static String msg;
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("id: "+session.getId()+" payload : " + payload);
        String[] parts = payload.split(" ");

        if(payload.startsWith("\"")){
            String username = parts[1];
            users.put(session,username);
            log.info(users);
        }
        if (parts[2].equals("/w")) {
            mode = true;
            sender = parts[0];
            user = parts[3];
        } else if (parts[2].equals("/q")) {
            mode = false;
        }

        if(mode) {
            msg = message.getPayload();
            if (!msg.startsWith(sender + " : /w")) {
                String[] msgPart = msg.split(" ");
                for (WebSocketSession key : users.keySet()) {
                    if (users.get(key).equals(sender)) {
                        TextMessage textMessage = new TextMessage(user + "에게 귓말 : " + msgPart[2]);
                        key.sendMessage(textMessage);
                    } else if (users.get(key).equals(user)) {
                        TextMessage textMessage = new TextMessage(sender + "님의 귓말 : " + msgPart[2]);
                        key.sendMessage(textMessage);
                    }
                }
            }
        }else {
            for(WebSocketSession sess: list) {
                if(!parts[2].equals("/q")){
                    sess.sendMessage(message);
                }
            }
        }


    }


    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        list.add(session);
        log.info(session + " 클라이언트 접속");
    }

    /* Client가 접속 해제 시 호출되는 메서드드 */

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info(session + " 클라이언트 접속 해제");
        users.remove(session);
        list.remove(session);
    }

    public Collection<String> usersList(){
        return users.values();
    }
}