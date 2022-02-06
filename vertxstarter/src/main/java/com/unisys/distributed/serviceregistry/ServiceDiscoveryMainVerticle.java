package com.unisys.distributed.serviceregistry;

import io.vertx.core.Vertx;

public class ServiceDiscoveryMainVerticle {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new HelloRestService());
    vertx.deployVerticle(new ServicePublisherVerticle());
    vertx.deployVerticle(new ServiceConsumerVerticle());
  }
}
