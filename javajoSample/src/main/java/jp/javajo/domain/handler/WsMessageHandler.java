package jp.javajo.domain.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * WebSocket処理いろいろ
 * Created by maaya
 */
@Component
public class WsMessageHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(WsMessageHandler.class);


    /**
     * せっしょんぷーる
     */
    private Map<String, WebSocketSession> sessionPool = new ConcurrentHashMap<>();

    /**
     * コネクションを設立したあとにセッションをプールしる
     *
     * @param session セッション
     * @throws Exception 例外
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.debug("afterConnectionEstablished");
        this.sessionPool.put(session.getId(), session);
    }

    /**
     * 切断された接続はプールから削除
     *
     * @param session セッション
     * @param status  ステータス
     * @throws Exception 例外
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.debug("afterConnectionClosed");
        this.sessionPool.remove(session.getId());
    }

    /**
     * ハンドリングしたテキストメッセージを送信！
     *
     * @param session セッション
     * @param message メッセージ
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.debug("handleTextMessage start");
        for (Iterator<Map.Entry<String, WebSocketSession>> iterator = this.sessionPool.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, WebSocketSession> entry = iterator.next();
            entry.getValue().sendMessage(message);
        }
        logger.debug("handleTextMessage end");
    }


}
