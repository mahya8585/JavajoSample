import tornado.web
import tornado.websocket
import tornado.ioloop
import os
import logging
import uuid
from tornado.options import define, options

"""
翻訳をwebsocketで表示するシステム
"""

define(u"port", default=8888, help=u"run on the given port", type=int)
define(u"debug", default=False, help=u"run in debug mode")


class Controller(tornado.web.Application):
    def __init__(self):
        """
        コントローラクラス
        """
        # URLの指定
        handlers = [
            (u"/", MainHandler),
            (u"/wss", SocketHandler),
        ]
        settings = dict(
            template_path=os.path.join(os.path.dirname(__file__), u"templates"),
            static_path=os.path.join(os.path.dirname(__file__), u"static"),

            autoescape=u"xhtml_escape",
            debug=options.debug,
        )
        tornado.web.Application.__init__(self, handlers, **settings)


class MainHandler(tornado.web.RequestHandler):
    def get(self):
        """
        Socket表示ページを返却
        :return: render index.html
        """
        self.render(u"index.html", messages=SocketHandler.cache)


class SocketHandler(tornado.websocket.WebSocketHandler):
    waiters = set()
    cache = []
    cache_size = 200

    def get_compression_options(self):
        # Non-None enables compression with default options.
        return {}

    def open(self):
        SocketHandler.waiters.add(self)

    def on_close(self):
        SocketHandler.waiters.remove(self)

    @classmethod
    def update_cache(cls, chat):
        """
        文章の追加があった場合にキャッシュに追加する
        :param chat: チャット文章
        :return:
        """
        cls.cache.append(chat)
        if len(cls.cache) > cls.cache_size:
            cls.cache = cls.cache[-cls.cache_size:]

    @classmethod
    def send_updates(cls, chat):
        """
        表示メッセージのアップデート
        :param chat:
        :return:
        """
        logging.info(u"sending message to %d waiters", len(cls.waiters))
        print(chat)
        for waiter in cls.waiters:
            try:
                waiter.write_message(chat)
            except:
                logging.error(u"Error sending message", exc_info=True)

    def on_message(self, message):
        """
        表示メッセージの整形
        :param message:
        :return:
        """
        logging.info(u"got message %r", message)
        parsed = tornado.escape.json_decode(message)
        username = "議事録ぱっちょ"

        chat = {
            u"id": str(uuid.uuid4()),
            #u"from": str(username, encoding=u'utf-8'),  # html表示時文字化け対策実施
            u"from": username,
            u"body": parsed[u"body"],
        }
        chat[u"html"] = tornado.escape.to_basestring(
            self.render_string(u"message.html", message=chat))

        SocketHandler.update_cache(chat)
        SocketHandler.send_updates(chat)


def main():
    tornado.options.parse_command_line()
    app = Controller()
    app.listen(options.port)
    print(u"サーバを起動します。")
    print(u"http://localhost:" + str(options.port) + u" にアクセスしてください")
    print(u"止めるときは Ctrl + c を押してください")
    tornado.ioloop.IOLoop.instance().start()


if __name__ == u"__main__":
    main()
