package jp.javajo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 接続テストクラス
 * Created by maaya
 */
@Controller
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    /**
     * 疎通確認
     */
    @RequestMapping(value = "/")
    String test() {
        logger.debug("testメソッド");
        return "hello";
    }

}
