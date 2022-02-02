package com.unisys.nonblocking.http.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.List;

public class RouterVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(RouterVerticle.class);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    HttpServer server = vertx.createHttpServer();
    //Router creation
    Router router = Router.router(vertx);
    //which enables parsers -  json-  JsonObject
    router.route().handler(BodyHandler.create());

    //new Route
    router.get("/api/hello").handler(ctx -> {
      System.out.println("hello");
      HttpServerResponse serverResponse = ctx.response();
      serverResponse.setStatusCode(200);
      serverResponse.putHeader("content-type", "application/json");
      JsonObject greet = new JsonObject().put("message", "Hello");
      serverResponse.end(greet.encodePrettily());
    });
    router.get("/api/hai").handler(ctx -> {
      System.out.println("hai");
      HttpServerResponse serverResponse = ctx.response();
      serverResponse.setStatusCode(200);
      serverResponse.putHeader("content-type", "application/json");
      JsonObject greet = new JsonObject().put("message", "Hai");
      serverResponse.end(greet.encodePrettily());
    });
    router.post("/api/greet").handler(ctx -> {
      HttpServerResponse serverResponse = ctx.response();
      //reading data:
      JsonObject payload = ctx.getBodyAsJson();
      System.out.println(payload.encodePrettily());

      serverResponse.setStatusCode(200);
      serverResponse.putHeader("content-type", "application/json");
      JsonObject greet = new JsonObject().put("message", "Hai -POST");
      serverResponse.end(greet.encodePrettily());
    });
    router.get("/api/good").handler(ctx -> {
      List<String> message = ctx.queryParam("message");
      System.out.println(message.size());
      ctx.response().end("good " + message.get(0));
    });

//path Parameters: dynamic url  /api/hello/subramanian | Ram | John
    router.get("/api/hello/:whom").handler(ctx -> {
      HttpServerResponse serverResponse = ctx.response();
      //using request object extract path parameters
      String whom = ctx.pathParam("whom");
      serverResponse.setStatusCode(200);
      serverResponse.putHeader("content-type", "application/json");
      JsonObject greet = new JsonObject().put("message", "greet " + whom);
      serverResponse.end(greet.encodePrettily());
    });


    server.requestHandler(router);
    server.listen(8080);

  }
}
