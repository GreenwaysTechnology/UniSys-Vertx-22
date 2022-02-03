package com.unisys.nonblocking.http.web.communication;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;

class HelloVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.get("/api/hello").handler(routingContext -> {
      routingContext.response().end("Hello");
    });

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(3000, httpServerAsyncResult -> {
        if (httpServerAsyncResult.succeeded()) {
          System.out.println("Provider(Hello) Verticle Server is Running " + httpServerAsyncResult.result().actualPort());
        }
      });
  }
}

class GreeterVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    Router router = Router.router(vertx);
    WebClient webClient = WebClient.create(vertx);

    router.route().handler(BodyHandler.create());
    router.get("/api/greet").handler(routingContext -> {
      //we communiate hello verticle and get data and send
     // HttpRequest<Buffer> httpRequest = webClient.get(3000, "localhost", "/api/hello");
      HttpRequest<Buffer> httpRequest = webClient.getAbs("http://localhost:3000/api/hello");
      httpRequest.send(response -> {
        if (response.succeeded()) {
          System.out.println(response.result().bodyAsString());
          routingContext.response().setStatusCode(200).end(response.result().bodyAsString());
        } else {
          routingContext.response().setStatusCode(200).end(response.cause().getMessage());
        }
      });

    });
    vertx.createHttpServer()
      .requestHandler(router)
      .listen(3001, httpServerAsyncResult -> {
        if (httpServerAsyncResult.succeeded()) {
          System.out.println("Consumer(Greeter) Verticle Server is Running " + httpServerAsyncResult.result().actualPort());
        }
      });
  }
}
//call third party web service communication.

class UsersVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    Router router = Router.router(vertx);
    WebClient webClient = WebClient.create(vertx);

    router.get("/api/users").handler(routingContext -> {

      webClient.getAbs("https://jsonplaceholder.typicode.com/users").send(res -> {
        System.out.println(res.result().bodyAsJsonArray());
        routingContext.response()
          .putHeader("content-type", "application/json")
          .setStatusCode(200).end(res.result().bodyAsJsonArray().encodePrettily());

      });

    });
    vertx.createHttpServer()
      .requestHandler(router)
      .listen(3002, httpServerAsyncResult -> {
        if (httpServerAsyncResult.succeeded()) {
          System.out.println("Users Verticle Server is Running " + httpServerAsyncResult.result().actualPort());
        }
      });
  }
}


public class WebClientVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(WebClientVerticle.class);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    vertx.deployVerticle(new GreeterVerticle());
    vertx.deployVerticle(new HelloVerticle());
    vertx.deployVerticle(new UsersVerticle());
  }
}
