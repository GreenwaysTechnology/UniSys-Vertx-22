package com.unisys.nonblocking.http.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;

public class WebExceptionVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(WebExceptionVerticle.class);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    Router userRouter = Router.router(vertx);
    Router appRouter = Router.router(vertx);
    appRouter.mountSubRouter("/api/users", userRouter);

    userRouter.get("/list/:name").handler(ctx -> {

      String name = ctx.pathParam("name");
      if (name.equals("admin")) {
        ctx.response().setStatusCode(200).end("You are valid User");
      } else {
        throw new RuntimeException("You are Invalid User!!!!");
      }
    }).failureHandler(fctx -> {
      int statuCode = fctx.statusCode();
      String message = fctx.failure().getMessage();
      JsonObject jsonObject = new JsonObject().put("code", 100).put("message", message);
      fctx.response().setStatusCode(statuCode).end(jsonObject.encodePrettily());
    });


    vertx.createHttpServer().requestHandler(appRouter).listen(8080, handler -> {
      if (handler.succeeded()) {
        System.out.println("Webserver is running at " + handler.result().actualPort());
      }
    });
  }
}
