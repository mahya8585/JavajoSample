from ws4py.client.tornadoclient import TornadoWebSocketClient
from tornado import ioloop

class AzureStt(TornadoWebSocketClient):
     def opened(self):
         for i in range(0, 200, 25):
             self.send("*" * i)

     def received_message(self, m):
         print(m)
         if len(m) == 175:
             self.close(reason='Bye bye')

     def closed(self, code, reason=None):
         ioloop.IOLoop.instance().stop()

# TODO ここをAzure Authかませてトランスレータと接続してみる
ws = AzureStt('ws://localhost:9090/ws', protocols=['http-only', 'chat'])
ws.connect()

ioloop.IOLoop.instance().start()
