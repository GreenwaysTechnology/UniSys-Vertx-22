package com.unisys.nonblocking.http.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;

public class SimpleWebAPI extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(SimpleWebAPI.class);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);

    HttpServerOptions options;
    HttpServer server = vertx.createHttpServer();
//    server.requestHandler(request -> {
//      HttpServerResponse serverResponse = request.response();
//      serverResponse.setStatusCode(200);
//      serverResponse.putHeader("content-type", "application/json");
//      JsonObject greet = new JsonObject().put("message", "Hello");
//      serverResponse.end(greet.encodePrettily());
//    });

    //Router creation
    Router router = Router.router(vertx);

    //new Route
    router.route(HttpMethod.GET, "/api/hello").handler(ctx -> {
      System.out.println("hello");
      HttpServerResponse serverResponse = ctx.response();
      serverResponse.setStatusCode(200);
      serverResponse.putHeader("content-type", "application/json");
      JsonObject greet = new JsonObject().put("message", "Hello");
      serverResponse.end(greet.encodePrettily());
    });
    router.route(HttpMethod.GET, "/api/hai").handler(ctx -> {
      System.out.println("hai");
      HttpServerResponse serverResponse = ctx.response();
      serverResponse.setStatusCode(200);
      serverResponse.putHeader("content-type", "application/json");
      JsonObject greet = new JsonObject().put("message", "Hai");
      serverResponse.end(greet.encodePrettily());
    });

    router.route(HttpMethod.POST, "/api/hai").handler(ctx -> {
      System.out.println("hai");
      HttpServerResponse serverResponse = ctx.response();
      serverResponse.setStatusCode(200);
      serverResponse.putHeader("content-type", "application/json");
      JsonObject greet = new JsonObject().put("message", "Hai -POST");
      serverResponse.end(greet.encodePrettily());
    });
    //route : wild card pattern  , any route
    router.route().handler(ctx -> {
      HttpServerResponse serverResponse = ctx.response();
      serverResponse.setStatusCode(200);
      serverResponse.putHeader("content-type", "application/json");
      JsonObject greet = new JsonObject().put("message", "Home");
      serverResponse.end(greet.encodePrettily());
    });
    server.requestHandler(router);
    server.listen(8080);

  }
}
