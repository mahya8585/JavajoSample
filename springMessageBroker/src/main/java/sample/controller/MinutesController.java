package sample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by ishida.m
 */
@Controller
public class MinutesController {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 初期画面表示.
     *
     * @return テンプレートHTML
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String viewIndex() {
        return "index";
    }

    /**
     * websocketを使ったメッセージ表示.
     *
     * @return テンプレートHTML
     */
    @RequestMapping(value = "/meeting", method = RequestMethod.GET)
    public String meeting() {
        return "meeting";
    }

    /**
     * publisher
     * @return
     */
    @RequestMapping(value="/push", method = RequestMethod.GET)
    public String sendMessage() {
        log.info("start send topic/message");
        simpMessagingTemplate.convertAndSend("/topic/message", "{\"message\": \"APIからこんにちは\"}");

        return "ok";
    }

}
