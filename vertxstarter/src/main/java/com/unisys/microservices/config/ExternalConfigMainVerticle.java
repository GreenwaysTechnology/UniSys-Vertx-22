package com.unisys.microservices.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;

class ApplicationVerticle extends AbstractVerticle {
  private void readFromFile() {
    ConfigStoreOptions options = new ConfigStoreOptions()
      .setType("file")
      .setFormat("json")
      .setConfig(new JsonObject().put("path", "conf/config.json"));
    ConfigRetriever retriever = ConfigRetriever.create(vertx,
      new ConfigRetrieverOptions().addStore(options));

    retriever.getConfig().onSuccess(config -> {
      System.out.println("Config is Ready");
      System.out.println(config.getString("appName"));
      System.out.println(config.getString("version"));
    }).onFailure(err -> {
      System.out.println(err);
    });

  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    // System.out.println("hello");
    //readFromFile
    //readFromFile();
    ConfigStoreOptions git = new ConfigStoreOptions()
      .setType("git")
      .setConfig(new JsonObject()
        .put("url", "https://github.com/cescoffier/vertx-config-test.git")
        .put("path", "local")
        .put("filesets",
          new JsonArray().add(new JsonObject().put("pattern", "*.json"))));

    ConfigRetriever retriever = ConfigRetriever.create(vertx,
      new ConfigRetrieverOptions().addStore(git));

    retriever.getConfig().onSuccess(jsonObject -> {
      System.out.println("inside config" + jsonObject);
    }).onFailure(err -> {
      System.out.println("error " + err.getMessage());

    });
    vertx.createHttpServer().requestHandler(httpServerRequest -> {

      httpServerRequest.response().end("Hello");
    }).listen(8080, ar -> {
      System.out.println("server is running");
    });

//
//    retriever.getConfig(config -> {
//      System.out.println("entering");
//      if (config.succeeded()) {
//        System.out.println("Config is Ready");
//        JsonObject configRes = config.result();
//        System.out.println(configRes.encodePrettily());
//      } else {
//        System.out.println("Config Error : " + config.cause());
//      }
//    });
  }
}

public class ExternalConfigMainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(ExternalConfigMainVerticle.class);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    vertx.deployVerticle(new ApplicationVerticle());
  }
}
