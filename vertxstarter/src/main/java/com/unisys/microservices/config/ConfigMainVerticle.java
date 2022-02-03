package com.unisys.microservices.config;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;

class GreeterVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    JsonObject config = config();
    System.out.println(config.encodePrettily());
    String message = config.getString("message", "default");
    int port = config.getInteger("port", 8080);
    System.out.println("=>" + message.toUpperCase());

    vertx.createHttpServer()
      .requestHandler(request -> {
        request.response().end(config().getString("message", "You are lucky!!"));
      })
      .listen(config().getInteger("http.port", 3000));
  }
}


public class ConfigMainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(ConfigMainVerticle.class);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    JsonObject config = new JsonObject().put("message", "Hello!!!")
      .put("port", 3000).put("http.port",3002);
    DeploymentOptions options = new DeploymentOptions().setConfig(config);
//    DeploymentOptions options = new DeploymentOptions();

    vertx.deployVerticle(new GreeterVerticle(), options);
  }
}
