package com.unisys.nonblocking.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.example.util.Runner;

public class SimpleHTTPVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(SimpleHTTPVerticle.class);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //writing http apps
    HttpServer httpServer = vertx.createHttpServer();

    //Your app code : Request and Response Handlers
    httpServer.requestHandler(request -> {
      HttpServerResponse response = request.response();
      response.end("Hello Vertx");
    });
    //start the server
    httpServer.listen(3000, httpServerAsyncResult -> {
      if (httpServerAsyncResult.succeeded()) {
        System.out.println("Server is Up @ " + httpServerAsyncResult.result().actualPort());
      } else {
        System.out.println(httpServerAsyncResult.cause());
      }
    });

  }
}
