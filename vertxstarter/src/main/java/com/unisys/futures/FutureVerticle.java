package com.unisys.futures;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.example.util.Runner;

class FutureTransferVerticle extends AbstractVerticle {

  //method which returns future -  callee
  public Future<String> sayHello() {
//    return Future.future(new Handler<Promise<String>>() {
//      @Override
//      public void handle(Promise<String> stringPromise) {
//        stringPromise.complete("Hello");
//      }
//    });
    //We are returning
    return Future.future(future -> future.complete("Hello"));
  }

  //method which throws error
  public Future<String> createErrorMessage() {
    return Future.future(f -> f.fail("Something went wrong!!"));
  }

  //biz logic which may send success or failure data
  public Future<String> login(String username, String password) {
    return Future.future(handler -> {
      if (username.equals("admin") && password.equals("admin")) {
        handler.complete("Login success");
      } else {
        handler.fail("Login Failed");
      }
    });
  }

  //future object sending

  public Future<String> loginV1(String username, String password) {
    if (username.equals("admin") && password.equals("admin")) {
      return Future.succeededFuture("Login is Successful");
    } else {
      return Future.failedFuture("Login failed");
    }
  }

  //callback function -HOF
  public void loginV2(String userName, String password, Handler<AsyncResult<String>> aHandler) {
    if (userName.equals("admin") && password.equals("admin")) {
      //encapsulate data
      aHandler.handle(Future.succeededFuture("Login is Successful"));
    } else {
      aHandler.handle(Future.failedFuture("Login failed"));
    }
  }


  @Override
  public void start() throws Exception {
    super.start();
    //caller
//    createFuture().onComplete(new Handler<AsyncResult<String>>() {
//      @Override
//      public void handle(AsyncResult<String> asyncResult) {
//             if(async.succ) {

//             } else {}
//      }
//    });
    sayHello().onComplete(asyncResult -> {
      if (asyncResult.succeeded()) {
        //read data
        System.out.println(asyncResult.result());
      } else {
        System.out.println(asyncResult.cause().getMessage());
      }
    });
    //listen for error
    createErrorMessage().onComplete(asyncResult -> {
      if (asyncResult.failed()) {
        System.out.println(asyncResult.cause());
      }
    });
    //listen for success or failer
    login("admin", "admin").onComplete(asyncResult -> {
      if (asyncResult.failed()) {
        System.out.println(asyncResult.cause());
      }
      System.out.println(asyncResult.result());
    });
    login("xxx", "xxx").onComplete(asyncResult -> {
      if (asyncResult.failed()) {
        System.out.println(asyncResult.cause());
      } else {
        System.out.println(asyncResult.result());
      }
    });
    //onsuccess method and onfailure , you can chain methods
    login("admin", "admin")
      .onSuccess(res -> System.out.println(res))
      .onFailure(err -> System.out.println(err));
    login("admin", "admin")
      .onSuccess(System.out::println)
      .onFailure(System.out::println);

    //
    loginV1("admin", "admin")
      .onSuccess(System.out::println)
      .onFailure(System.out::println);
    //hof - function as parameter
    loginV2("admin", "admin", handler -> {
      if (handler.succeeded()) {
        System.out.println(handler.result());
      } else {
        System.out.println(handler.cause());
      }
    });
  }
}


public class FutureVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    System.out.println("deploying...");
    Runner.runExample(FutureVerticle.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    System.out.println("deploying...");

    vertx.deployVerticle(new FutureTransferVerticle());
  }
}
