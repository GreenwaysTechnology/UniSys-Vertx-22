package com.unisys.callback;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.example.util.Runner;

public class CompositFutureVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(CompositFutureVerticle.class);
  }

  public Future<String> startDbServer() {
    System.out.println("Db Server Started");
    return Future.succeededFuture("Db server is up");
  }

  public Future<String> startWebServer() {
    System.out.println("WebServer Server Started");
    //return Future.succeededFuture("Web server is up");
    return Future.failedFuture("Port is already In Use");
  }

  public Future<String> startConfigServer() {
    System.out.println("Config Server Started");
    return Future.succeededFuture("Config Server is up");
  }


  @Override
  public void start() throws Exception {
    super.start();

    Future<String> dbServer = startDbServer();
    Future<String> webServer = startWebServer();
    Future<String> configServer = startConfigServer();
    CompositeFuture.all(configServer, dbServer, webServer).onComplete(ar -> {
      if (ar.succeeded()) {
        System.out.println("All Server is Up");
      } else {
        System.out.println(ar.cause());
      }
    });
  }
}
