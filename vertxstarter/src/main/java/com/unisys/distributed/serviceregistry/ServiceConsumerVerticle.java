package com.unisys.distributed.serviceregistry;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;

public class ServiceConsumerVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    ServiceDiscoveryOptions discoveryOptions = new ServiceDiscoveryOptions();
    //enable discovery server : apache zoo keeper
    discoveryOptions.setBackendConfiguration(new JsonObject()
      .put("connection", "127.0.0.1:2181")
      .put("ephemeral", true)
      .put("guaranteed", true)
      .put("basePath", "/services/my-backend")
    );
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx, discoveryOptions);

    vertx.createHttpServer().requestHandler(req -> {
      HttpEndpoint.getWebClient(discovery, new JsonObject().put("name", "myrecord"), sar -> {
        // get web client
        WebClient webClient = sar.result();
        webClient.get("/api/hello").send(res -> {
          System.out.println("Response is ready!");
          req.response().end(res.result().bodyAsString());
        });
        req.response().endHandler(ar -> {
          //remove /release discovery record
          ServiceDiscovery.releaseServiceObject(discovery, webClient);
        });
      });


    }).listen(3000, ar -> {
      System.out.println("Service Consumer Service ");
    });


  }
}
