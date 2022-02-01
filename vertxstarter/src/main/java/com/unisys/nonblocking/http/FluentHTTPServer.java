package com.unisys.nonblocking.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.example.util.Runner;

public class FluentHTTPServer extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(FluentHTTPServer.class);
  }

  private static void handle(HttpServerRequest request) {
    request.response().end("Hello Http Server");
  }

  private static void startServer(AsyncResult<HttpServer> httpServerAsyncResult) {
    if (httpServerAsyncResult.succeeded()) {
      System.out.println("Server is Up @ " + httpServerAsyncResult.result().actualPort());
    } else {
      System.out.println(httpServerAsyncResult.cause());
    }
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //writing http apps
//    vertx.createHttpServer()
//      .requestHandler(request -> request.response().end("Hello Http Server"))
//      .listen(3000, httpServerAsyncResult -> {
//        if (httpServerAsyncResult.succeeded()) {
//          System.out.println("Server is Up @ " + httpServerAsyncResult.result().actualPort());
//        } else {
//          System.out.println(httpServerAsyncResult.cause());
//        }
//      });

    vertx.createHttpServer()
      .requestHandler(FluentHTTPServer::handle)
      .listen(3000, FluentHTTPServer::startServer);

  }
}
