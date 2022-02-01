package com.unisys.promises;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.example.util.Runner;

public class DeployHttpVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(DeployHttpVerticle.class);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);

//        vertx.deployVerticle(new HttpVerticle(), ar -> {
//      if (ar.succeeded()) {
//        System.out.println("Verticle deloyed  : " + ar.result());
//      }
//    });
    vertx.deployVerticle(HttpVerticle.class.getName(), ar -> {
      if (ar.succeeded()) {
        System.out.println(HttpVerticle.class.getName() + " Deployment ID " +  ar.result());
      }
    });
    vertx.deployVerticle(HttpVerticle.class.getName(), ar -> {
      if (ar.succeeded()) {
        System.out.println(HttpVerticle.class.getName() + " Deployment ID " +  ar.result());
      }
    });
  }
}
