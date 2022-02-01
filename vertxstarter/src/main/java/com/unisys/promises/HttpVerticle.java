package com.unisys.promises;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;

public class HttpVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    //create
    HttpServer server = vertx.createHttpServer().requestHandler(req -> {
      req.response().end("Hello");
    });
    //start server
    server.listen(8080, res -> {
      if (res.succeeded()) {
        System.out.println("Server Started");
        startPromise.complete();//when ever completed is called , then verticle succfully deployment
      } else {
        startPromise.fail(res.cause());
      }
    });

  }
}
