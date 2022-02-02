package com.unisys.nonblocking.http.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class ResourceRouters extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(ResourceRouters.class);
  }

  private static void list(RoutingContext ctx) {
    ctx.response().end("Users");
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //SubRouters
    Router userRouter = Router.router(vertx);
    Router productsRouter = Router.router(vertx);
    Router customerRouter = Router.router(vertx);
    //Main/app router
    Router appRouter = Router.router(vertx);
    //initialize global Parser
    appRouter.route().handler(BodyHandler.create());
    userRouter.get("/list").handler(UserRouter::list);
    userRouter.post("/create").handler(UserRouter::create);
    productsRouter.get("/list").handler(ProductsRouter::list);
    customerRouter.get("/list").handler(CustomersRouter::list);

    //Mount/Bind appRouter with subRouters
    appRouter.mountSubRouter("/api/users", userRouter);
    appRouter.mountSubRouter("/api/products", productsRouter);
    appRouter.mountSubRouter("/api/customers", customerRouter);

    vertx.createHttpServer().requestHandler(appRouter).listen(8080, handler -> {
      if (handler.succeeded()) {
        System.out.println("Webserver is running at " + handler.result().actualPort());
      }
    });


  }

  //inner class
  private static class UserRouter {
    public static void create(RoutingContext ctx) {
      JsonObject user = ctx.getBodyAsJson();
      System.out.println(user.encodePrettily());
      ctx.response().setStatusCode(201).end("User saved");
    }

    public static void list(RoutingContext ctx) {
      ctx.response().setStatusCode(200).end("User list");
    }
  }

  private static class ProductsRouter {
    public static void create(RoutingContext ctx) {
      JsonObject user = ctx.getBodyAsJson();
      System.out.println(user.encodePrettily());
      ctx.response().setStatusCode(201).end("Product saved");
    }

    public static void list(RoutingContext ctx) {
      ctx.response().setStatusCode(200).end("Product list");
    }
  }

  private static class CustomersRouter {
    public static void create(RoutingContext ctx) {
      JsonObject user = ctx.getBodyAsJson();
      System.out.println(user.encodePrettily());
      ctx.response().setStatusCode(201).end("Customers saved");
    }

    public static void list(RoutingContext ctx) {
      ctx.response().setStatusCode(200).end("Customers list");
    }
  }
}
