package com.unisys.nonblocking.timers;

import io.vertx.core.*;
import io.vertx.example.util.Runner;

public class TimersVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(TimersVerticle.class);
  }

  private void blockMe(String str) {
    System.out.println(str);
  }

  //  public void delay(long delayTime) {
//    vertx.setTimer(delayTime, handler -> {
//      System.out.println("I am delayed output" + "Timeout is " + delayTime);
//    });
//  }
//  public Future<String> delay(long delayTime) {
//    Future<String> future = Future.future(ar -> {
//      vertx.setTimer(delayTime, handler -> {
//        //System.out.println("I am delayed output" + "Timeout is " + delayTime);
//        String response = "I am delayed output" + "Timeout is " + delayTime;
//      });
//    });
//    //return future;
//
//  }

  public void delay(long delayTime, Handler<AsyncResult<String>> aHandler) {
    vertx.setTimer(delayTime, handler -> {
      String response = "I am delayed output" + "Timeout is " + delayTime;
      aHandler.handle(Future.succeededFuture(response));
    });
  }

  //tick
  public void tick(Handler<AsyncResult<String>> aHandler) {
    long timerId = vertx.setPeriodic(1000, handler -> {
      aHandler.handle(Future.succeededFuture(Math.random() + ""));
    });
    //stop ticking
    vertx.setTimer(10000, stopHandler -> {
      System.out.println("Stop Polling");
      vertx.cancelTimer(timerId);
    });

//    Promise<String> promise = Promise.promise();
//    vertx.setPeriodic(1000, handler -> promise.complete("Success"));
//    return promise.future();

  }


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    blockMe("start");
//    delay(5000);
//    delay(3000);
    delay(500, asyncResult -> {
      System.out.println(asyncResult.result());
    });
    tick(res -> {
      System.out.println(res.result());
    });
//    tick(null).onComplete(System.out::println);
    blockMe("end");
  }
}
