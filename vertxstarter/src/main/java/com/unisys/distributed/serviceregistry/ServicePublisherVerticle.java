package com.unisys.distributed.serviceregistry;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;


public class ServicePublisherVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    //Service Discovery Options
//    ServiceDiscovery.create(vertx)
//      .registerServiceImporter(new ZookeeperServiceImporter()
//        , new JsonObject().put("connection", "127.0.0.1:2181"));
    ServiceDiscoveryOptions discoveryOptions = new ServiceDiscoveryOptions();
    //enable discovery server : apache zoo keeper
    discoveryOptions.setBackendConfiguration(new JsonObject()
      .put("connection", "127.0.0.1:2181")
      .put("ephemeral", true)
      .put("guaranteed", true)
      .put("basePath", "/services/my-backend")
    );
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx, discoveryOptions);

    //Record Creation,Record type is Resource Type;storing webclient Resource into registry
    Record httpEndPointRecord = HttpEndpoint.createRecord("myrecord", "localhost", 3001, "/api/hello");

    //publish: add record into zookeeper
    discovery.publish(httpEndPointRecord, ar -> {
      if (ar.succeeded()) {
        System.out.println("Successfully published ..>>>>" + ar.result().toJson());
      } else {
        System.out.println(" Not Published " + ar.cause());
      }
    });

  }
}
