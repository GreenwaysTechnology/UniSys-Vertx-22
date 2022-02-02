package com.unisys.nonblocking.http.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.ResponseContentTypeHandler;

public class ContentNegoation extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(ContentNegoation.class);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    Router userRouter = Router.router(vertx);
    Router appRouter = Router.router(vertx);

    //adding header automatically into response
    appRouter.route().handler(ResponseContentTypeHandler.create());

    appRouter.mountSubRouter("/api/users", userRouter);

    userRouter.get("/")
      .produces("application/json")
      .handler(ctx -> {
        System.out.println(ctx.getAcceptableContentType());
//        if (ctx.getAcceptableContentType().equals("text/plain")) {
//          ctx.response().end("Text content");
//        } else {
//          ctx.response().end(new JsonObject().put("content", "json").encodePrettily());
//        }
        ctx.response().end(new JsonObject().put("content", "json").encodePrettily());
      });

    vertx.createHttpServer().requestHandler(appRouter).listen(8080, handler -> {
      if (handler.succeeded()) {
        System.out.println("Webserver is running at " + handler.result().actualPort());
      }
    });
  }
}
