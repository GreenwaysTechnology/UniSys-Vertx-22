package com.unisys;

import io.vertx.core.Vertx;

public class GreeterDeployerMain {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
   //deploy the verticle on vertx engine
//    vertx.deployVerticle(new GreetingVerticle());
    vertx.deployVerticle(GreetingVerticle.class.getName());
    vertx.deployVerticle("com.unisys.GreetVerticle");
    vertx.close();
  }
}
