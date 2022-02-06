package com.unisys.rx;

import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Promise;
import io.vertx.example.util.Runner;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.eventbus.EventBus;
import io.vertx.rxjava3.core.http.HttpServer;
import io.vertx.rxjava3.ext.web.client.HttpResponse;
import io.vertx.rxjava3.ext.web.client.WebClient;
import io.vertx.rxjava3.ext.web.codec.BodyCodec;

class ReactiveWebClient extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startFuture) throws Exception {
    super.start(startFuture);
    vertx.createHttpServer().requestHandler(req -> {
      WebClient webClient = WebClient.create(vertx);
      Single<HttpResponse<String>> request = webClient.get(8081, "localhost", "/").as(BodyCodec.string()).rxSend();
      //fire the http call
      request.subscribe(result -> {
        req.response().end(result.body());
      });
    }).rxListen(3000).subscribe();
  }
}


class GreeterReactive extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startFuture) throws Exception {
    super.start(startFuture);
    //reactive web server
    EventBus eventBus = vertx.eventBus();

    eventBus.consumer("com.unisys").toFlowable().subscribe(message -> {
      System.out.println(message.body().toString());
      message.reply("PONG");
    });
    vertx.setPeriodic(1000, v -> {
      eventBus.rxRequest("com.unisys", "PING").subscribe(reply -> {
        System.out.println("Received Reply " + reply.body());
      });
    });

    //This is not enabled with Reactive Streams
//    vertx.createHttpServer().requestHandler(httpServerRequest -> {
//      httpServerRequest.response().end("Hello");
//
//    }).rxListen(8080).subscribe();
    HttpServer httpServer = vertx.createHttpServer();
    httpServer.requestStream().toFlowable().subscribe(req -> {
      req.response().end("Stream data over http using web client");
    });
    httpServer.rxListen(8081).subscribe(System.out::println);


  }
}

public class ReactiveVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(ReactiveVerticle.class);
  }

  @Override
  public void start(Promise<Void> startFuture) throws Exception {
    super.start(startFuture);
    vertx.rxDeployVerticle(new GreeterReactive())
      .subscribe(data -> System.out.println(data), System.out::println);
    vertx.deployVerticle(new ReactiveWebClient());
  }
}
