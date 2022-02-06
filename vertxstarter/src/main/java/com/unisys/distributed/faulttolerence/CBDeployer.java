package com.unisys.distributed.faulttolerence;

import io.vertx.core.Vertx;

public class CBDeployer {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new SomeService());
    vertx.deployVerticle(new CircuitBreakerPatternVerticle());
  }
}
