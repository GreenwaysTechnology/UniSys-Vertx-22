package com.unisys;

import io.vertx.core.AbstractVerticle;

public class GreetingVerticle extends AbstractVerticle {
  //logic
  @Override
  public void start() throws Exception {
    super.start();
    System.out.println("Greeting Verticle is ready");
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    System.out.println("Greeting Verticle is removed");

  }
}
