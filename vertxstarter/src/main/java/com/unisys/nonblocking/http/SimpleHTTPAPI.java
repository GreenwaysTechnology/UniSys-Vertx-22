package com.unisys.nonblocking.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.example.util.Runner;

public class SimpleHTTPAPI extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(SimpleHTTPAPI.class);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    vertx.createHttpServer()
      .requestHandler(request -> {
        //build rest api
        if (request.uri().equals("/api/users") && request.method() == HttpMethod.GET) {
          request.response().setStatusCode(200).end("users-GET");
        }
        if (request.uri().equals("/api/users") && request.method() == HttpMethod.POST) {
          request.response().setStatusCode(200).end("users-POST");
        }
        if (request.uri().equals("/api/users") && request.method() == HttpMethod.PUT) {
          request.response().setStatusCode(200).end("users-PUT");
        }
      })
      .listen(3000,server -> {
        if (server.succeeded()) {
          System.out.println(server.result().actualPort() + " Server is running");
        } else {
          System.out.println(server.cause());
        }
      });


  }
}
