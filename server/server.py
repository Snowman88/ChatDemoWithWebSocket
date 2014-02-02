from tornado import web, websocket, ioloop
import json
from tornado.options import options

options.define("port", default=8888, type=int)

cl = []

class SocketHandler(websocket.WebSocketHandler):
    def open(self):
        if self not in cl:
            cl.append(self)


    def on_close(self):
        if self in cl:
            cl.remove(self)

    def on_message(self, message):
        data = {"message": message}
        data = json.dumps(data)
        for c in cl:
            c.write_message(data)

app = web.Application([
    (r"/ws", SocketHandler),
])

if __name__ == "__main__":
    app.listen(options.port)
    ioloop.IOLoop.instance().start()




