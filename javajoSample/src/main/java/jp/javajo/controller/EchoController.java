package jp.javajo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * WebSocketを使ったechoクラス
 * Created by maaya
 */
@Controller
public class EchoController {
    private static final Logger logger = LoggerFactory.getLogger(EchoController.class);

    /**
     * 開始ページの返却
     *
     * @return テンプレートのパス
     */
    @RequestMapping("/javajo")
    public String showStartPage() {
        logger.debug("javajo");
        return "index";
    }

}
