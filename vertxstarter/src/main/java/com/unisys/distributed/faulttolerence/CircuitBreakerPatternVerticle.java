package com.unisys.distributed.faulttolerence;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class CircuitBreakerPatternVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    CircuitBreakerOptions options = new CircuitBreakerOptions();
    options.setMaxFailures(2);// no of failures will be allowed , after that , ciruit will open
    options.setTimeout(3000); // consider a failure if the operation does not succeed in time
    options.setFallbackOnFailure(true); // if any failure, should i handle fallback or not
    options.setResetTimeout(5000); // time spent in open state before attempting to retry.

    CircuitBreaker circuitBreaker = CircuitBreaker.create("my-circuit-breaker", vertx, options);
    vertx.createHttpServer().requestHandler(req -> {
      circuitBreaker.executeWithFallback(future -> {
        //risky code
        WebClient client = WebClient.create(vertx);
        client.get(3000, "localhost", "/").send(ar -> {
          // Obtain response
          HttpResponse<Buffer> response = ar.result();
          if (response.statusCode() != 200) {
            future.fail("HTTP error");
          } else {
            future.complete(response.bodyAsString());
          }
        });
      }, v -> {
        // Executed when the circuit is opened
        return "Hello, I am fallback";
      }).onComplete(ar -> {
        // Do something with the result
        if (ar.succeeded()) {
          //System.out.println(ar.result());
          req.response().end(ar.result());
        } else {
          System.out.println(ar.cause());

        }
      });
    }).listen(8080);

  }
}
