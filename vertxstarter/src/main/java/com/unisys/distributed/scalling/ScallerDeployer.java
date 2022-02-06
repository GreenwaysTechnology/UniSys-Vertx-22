package com.unisys.distributed.scalling;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.example.util.Runner;
import io.vertx.rxjava3.core.AbstractVerticle;


public class ScallerDeployer extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(ScallerDeployer.class);
  }

  @Override
  public void start(Promise<Void> startFuture) throws Exception {
    super.start(startFuture);
    DeploymentOptions options = new DeploymentOptions().setInstances(5);
    vertx.rxDeployVerticle(GreeterService.class.getName(), options).subscribe(res -> {
      System.out.println(GreeterService.class.getName() + " " + res);
    }, err -> {
      System.out.println(err);
    });
  }
}
