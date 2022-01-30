package com.unisys;

import io.vertx.core.Vertx;

public class HelloWorldVertx {
  public static void main(String[] args) {
    //Vertx Engine Creation
    Vertx vertx = Vertx.vertx();
    System.out.println(vertx);
    System.out.println(Thread.currentThread().getName());
    //create simple non blocking web server
    vertx.createHttpServer().requestHandler(req -> {
      System.out.println(Thread.currentThread().getName());
      req.response().end("Hello Vertx");
    }).listen(8080);

  }
}
