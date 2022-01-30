package com.unisys;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;

public class GreeterDeployer extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(GreeterDeployer.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    vertx.deployVerticle(new GreetingVerticle());
  }
}
