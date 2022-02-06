package com.unisys.distributed.serviceregistry;

import io.vertx.core.AbstractVerticle;

public class HelloRestService extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    vertx.createHttpServer().requestHandler(request -> {
      if (request.path().equals("/api/hello")) {
        request.response().end("<h1>Hello! I am coming via Service Discovery<h1>");
      }
    }).listen(3001);
  }
}
