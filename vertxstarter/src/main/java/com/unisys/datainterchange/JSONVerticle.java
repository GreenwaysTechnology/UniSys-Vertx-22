package com.unisys.datainterchange;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;

public class JSONVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(JSONVerticle.class);
  }

  public Future<JsonArray> getUser() {
//    JsonObject user = new JsonObject();
//    user.put("id", 1);
//    user.put("firstName", "Subramanian");
//    user.put("lastName", "Murugan");
    //Vertx Flutent Pattern
    JsonObject skill = new JsonObject().put("skillId", 100).put("skill", "Vertx");

    JsonObject user = new JsonObject()
      .put("id", 1)
      .put("firstName", "Subramanian")
      .put("lastName", "Murugan")
      .mergeIn(skill)
      .put("address", new JsonObject()
        .put("city", "Coimbatore")
        .put("state", "Tamil Nadu"));

    JsonArray users = new JsonArray()
      .add(user)
      .add(user)
      .add(new JsonObject()
        .put("id", 1)
        .put("firstName", "Subramanian")
        .put("lastName", "Murugan")
        .put("address", new JsonObject()
          .put("city", "Coimbatore")
          .put("state", "Tamil Nadu")));


    return Future.succeededFuture(users);
  }


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    getUser().onSuccess(user -> {
      System.out.println(user.encodePrettily());

    });
  }
}
