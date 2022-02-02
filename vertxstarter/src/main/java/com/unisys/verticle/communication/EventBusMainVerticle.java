package com.unisys.verticle.communication;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;

class Address {
  public final static String PUB_SUB_ADDRESS = "news.in.covid";
  public final static String POINT_TO_POINT = "covid.fin.request";
  public final static String REQUEST_REPLY = "covid.lab.report";
}

///////////////////////////////////////PUB-SUB//////////////////////////////////////////////////////////
class CovidCommunicatorVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    Router appRouter = Router.router(vertx);
    //pub-sub
    Router broadcastRouter = Router.router(vertx);
    //point to point
    Router pointToPointRouter = Router.router(vertx);
    //Request-Reply
    Router requestReplyRouter = Router.router(vertx);

    appRouter.mountSubRouter("/api/brodcast", broadcastRouter);
    appRouter.mountSubRouter("/api/requestfin", pointToPointRouter);
    appRouter.mountSubRouter("/api/status", requestReplyRouter);


    broadcastRouter.get("/:message").handler(ctx -> {
      //Event Bus to send message to other Verticles
      String message = ctx.pathParam("message");
      EventBus eventBus = vertx.eventBus();
      //publish - one to many
      eventBus.publish(Address.PUB_SUB_ADDRESS, message);
      ctx.response().end("Message has Broad casted");
    });

    pointToPointRouter.get("/:howmuch").handler(ctx -> {
      //Event Bus to send message to other Verticles
      String message = ctx.pathParam("howmuch");
      EventBus eventBus = vertx.eventBus();
      //send - one to one
      eventBus.send(Address.POINT_TO_POINT, message);
      ctx.response().end("Message has been sent");
    });

    requestReplyRouter.get("/:name").handler(ctx -> {
      //Event Bus to send message to other Verticles
      String name = ctx.pathParam("name");
      String messageToSend = "Report of " + name;
      //send message and get reply : request-reply with ack
      vertx.eventBus().request(Address.REQUEST_REPLY, messageToSend, asyncResult -> {
        if (asyncResult.succeeded()) {
          System.out.println(asyncResult.result().body());
          ctx.response().end(asyncResult.result().body().toString());
        } else {
          System.out.println(asyncResult.cause());
        }
      });

    });
    vertx.createHttpServer().requestHandler(appRouter).listen(8080, handler -> {
      if (handler.succeeded()) {
        System.out.println("Event Bus Web Server is running at " + handler.result().actualPort());
      }
    });

  }
}

//PUB - SUB Listener
class NewsSevenVerticle extends AbstractVerticle {
  public void consume() {
    //Get the Reference of Event Bus
    EventBus eventBus = vertx.eventBus();
    MessageConsumer<String> consumer = eventBus.consumer(Address.PUB_SUB_ADDRESS);
    //process the message
    consumer.handler(msg -> {
      System.out.println(this.getClass().getSimpleName() + " => " + msg.body());
    });
  }

  @Override
  public void start() throws Exception {
    super.start();
    consume();
  }
}

class BBCVerticle extends AbstractVerticle {
  public void consume() {
    EventBus eventBus = vertx.eventBus();
    MessageConsumer<String> messageConsumer = eventBus.consumer(Address.PUB_SUB_ADDRESS);
    messageConsumer.handler(message -> {
      System.out.println(this.getClass().getSimpleName() + " => " + message.body());
    });
  }

  @Override
  public void start() throws Exception {
    super.start();
    consume();
  }
}

class NDTVVerticle extends AbstractVerticle {
  public void consume() {
    EventBus eventBus = vertx.eventBus();
    MessageConsumer<String> messageConsumer = eventBus.consumer(Address.PUB_SUB_ADDRESS);
    messageConsumer.handler(message -> {
      System.out.println(this.getClass().getSimpleName() + " => " + message.body());
    });
  }

  @Override
  public void start() throws Exception {
    super.start();
    consume();
  }
}
///////////////////////////////////////PUB-SUB//////////////////////////////////////////////////////////

///////////////////////////////////////POINT TO POINT//////////////////////////////////////////////////////////

class CenertalFinanceVerticle extends AbstractVerticle {

  public void consume() {
    EventBus eventBus = vertx.eventBus();
    MessageConsumer<String> messageConsumer = eventBus.consumer(Address.POINT_TO_POINT);
    //handle /process the message/news
    messageConsumer.handler(news -> {
      System.out.println("Request 1 -  : " + news.body());
    });
  }

  @Override
  public void start() throws Exception {
    super.start();
    consume();
  }
}

///////////Request-Reply///////////////////////////////////////////////////////////////////////
class LabVerticle extends AbstractVerticle {
  public void consume() {
    EventBus eventBus = vertx.eventBus();
    //pub-sub
    MessageConsumer<String> messageConsumer = eventBus.consumer(Address.REQUEST_REPLY);
    //handle /process the message/news
    messageConsumer.handler(news -> {
      System.out.println("Request -  : " + news.body());
      //sending reply /ack
      news.reply(news.body() + " who  is Critical , Need More attention");
    });
  }

  @Override
  public void start() throws Exception {
    super.start();
    consume();
  }
}


public class EventBusMainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(EventBusMainVerticle.class);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    vertx.deployVerticle(new CovidCommunicatorVerticle());
    /////pub-sub
    vertx.deployVerticle(new NewsSevenVerticle());
    vertx.deployVerticle(new BBCVerticle());
    vertx.deployVerticle(new NDTVVerticle());
    //point to point
    vertx.deployVerticle(new CenertalFinanceVerticle());
    //request-reply
    vertx.deployVerticle(new LabVerticle());

  }
}
