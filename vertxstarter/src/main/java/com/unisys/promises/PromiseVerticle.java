package com.unisys.promises;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.example.util.Runner;

class LoginVerticle extends AbstractVerticle {

  //Return Promise, later convert into Future if you want, or any api dirctly uses promises
  public Promise<String> auth(String userName, String password) {
    //create Promise object
    Promise<String> promise = Promise.promise();
    if (userName.equals("admin") && password.equals("admin")) {
      promise.complete("Login success");
    } else {
      promise.fail("Login failed");
    }
    return promise;
  }

  public Future<String> authV1(String userName, String password) {
    //create Promise object
    Promise<String> promise = Promise.promise();
    if (userName.equals("admin") && password.equals("admin")) {
      promise.complete("Login success");
    } else {
      promise.fail("Login failed");
    }
    return promise.future();
  }

  @Override
  public void start() throws Exception {
    super.start();
    //future method will convert promise into Future
    auth("admin", "admin").future()
      .onSuccess(System.out::println)
      .onFailure(System.out::println);

    authV1("admin", "admin")
      .onSuccess(System.out::println)
      .onFailure(System.out::println);
  }
}

public class PromiseVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(PromiseVerticle.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    vertx.deployVerticle(new LoginVerticle());
  }
}
