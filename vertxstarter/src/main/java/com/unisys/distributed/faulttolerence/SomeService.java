package com.unisys.distributed.faulttolerence;

import io.vertx.core.AbstractVerticle;

public class SomeService extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    vertx.createHttpServer().requestHandler(r -> {
      vertx.setTimer(5000, ar -> {
        r.response().end("I am fine but delayed!");
      });
    }).listen(3000);
  }
}
