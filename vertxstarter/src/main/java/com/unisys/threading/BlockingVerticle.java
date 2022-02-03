package com.unisys.threading;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;


class UserVerticle extends AbstractVerticle {

  private void resultHandler(AsyncResult<String> resultHandler) {
    System.out.println("Result Handler" + Thread.currentThread().getName());
    if (resultHandler.succeeded()) {
      System.out.println("Blocking api Result goes Ready Here");
      System.out.println(resultHandler.result());
    } else {
      System.out.println(resultHandler.cause().getMessage());
    }
  }

  //blocking method : this operation should be handled by worker pool thread
  public void findAll(Promise<String> promise) {
    System.out.println("findAll : " + Thread.currentThread().getName());
    try {
      Thread.sleep(4000);
      System.out.println("Wake Up for sending data to Non blocking Service");
      //this result will be accessed inside non blocking code
      promise.complete("Hey this is blocking Result");

    } catch (InterruptedException es) {
      promise.fail("Something went wrong in blocking service");
    }
  }
  //non blocking method


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //how to wrap blocking code.
    vertx.executeBlocking(this::findAll, this::resultHandler);

    //web end point
    Router router = Router.router(vertx);

    //non blocking handler :  handler
    //router.get("/api/blocking").handler(routingContext -> {});
    router.get("/api/blocking").blockingHandler(rc -> {
      System.out.println("Http Request Served => " + Thread.currentThread().getName());
      try {
        //blocking code
        Thread.sleep(5000);
        String blockingResult = "Blocking result";
        rc.response().end(blockingResult);
      } catch (Exception e) {

      }
    });

    vertx.createHttpServer().requestHandler(router).listen(8888, ar -> {
      System.out.println("Non Blocking and Blocking Server " + ar.result().actualPort());
    });


  }
}


class GreeterVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    System.out.println(GreeterVerticle.class.getName() + " is Running on " + Thread.currentThread().getName());
    //blocking code.
    try {
      Thread.sleep(8000);
      System.out.println("Hi i am  output after blocking!!!");
    } catch (InterruptedException e) {
      System.out.println(e.getMessage());
    }
  }
}


public class BlockingVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(BlockingVerticle.class);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);

    System.out.println(BlockingVerticle.class.getName() + " is Running on " + Thread.currentThread().getName());

    HttpServerOptions serverOptions = new HttpServerOptions()
      .setPort(8080).setHost("localhost");
    vertx.createHttpServer(serverOptions).requestHandler(httpServerRequest -> {
      DeliveryOptions options = new DeliveryOptions().addHeader("myheader", "value");
      vertx.eventBus().send("hello", options);
    }).listen(httpServerAsyncResult -> {
      System.out.println(httpServerAsyncResult.result().actualPort());
    });

    //worker verticle
    DeploymentOptions deploymentOptions = new DeploymentOptions().setWorker(true);
    vertx.deployVerticle(new GreeterVerticle(), deploymentOptions);

    //Regular verticle with blocking code
    vertx.deployVerticle(new UserVerticle());
//

//    for (int i = 1; i < 27; i++) {
//      vertx.deployVerticle(new GreeterVerticle());
//
//    }
  }
}
