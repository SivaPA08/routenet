
from http.server import BaseHTTPRequestHandler, HTTPServer

GATEWAY_SECRET = "super-secret-value"


class Handler(BaseHTTPRequestHandler):

    def do_GET(self):
        if self.path != "/auth":
            self.send_response(404)
            self.end_headers()
            return

        if self.headers.get("X-Internal-Token") != GATEWAY_SECRET:
            self.send_response(403)
            self.end_headers()
            self.wfile.write(b"forbidden")
            return

        user_id = self.headers.get("X-User-Id")
        if not user_id:
            self.send_response(401)
            self.end_headers()
            self.wfile.write(b"unauthorized")
            return

        role = self.headers.get("X-User-Role", "unknown")

        self.send_response(200)
        self.end_headers()
        self.wfile.write(
            f"authorized user={user_id} role={role}".encode()
        )


if __name__ == "__main__":
    print("Auth endpoint running on :8081 (/auth only)")
    HTTPServer(("", 8081), Handler).serve_forever()
