package com.unisys.databases;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

public class JDBCClientVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(JDBCClientVerticle.class);
  }

  public void prepareDatabase() {
    JsonObject connectionString = new JsonObject()
      .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver")
      .put("max_pool_size", 30)
      .put("user", "SA")
      .put("password", "");
    JDBCClient jdbcClient = JDBCClient.createShared(vertx, connectionString);
    //Establishing connection
    jdbcClient.getConnection(connection -> {
      if (connection.succeeded()) {
        System.out.println("Connection is success");
        //Connection Reference
        SQLConnection sqlConnection = connection.result();
        String CREATE_TABLE = "create table user(id int primary key, name varchar(255))";
        sqlConnection.execute(CREATE_TABLE, tableCreate -> {
          if (tableCreate.succeeded()) {
            String INSERT_QUERY = "insert into user values(1, 'subramanian')";
            sqlConnection.execute(INSERT_QUERY, tableInsert -> {

              if (tableInsert.failed()) {
                System.out.println(tableInsert.cause());
              } else {
                //show sample data; write select query
                String SELECT_QUERY = "select * from user";
                sqlConnection.query(SELECT_QUERY, selectResult -> {
                  for (JsonArray line : selectResult.result().getResults()) {
                    System.out.println(line.encode());
                  }
                  // and close the connection
                  sqlConnection.close(done -> {
                    if (done.failed()) {
                      throw new RuntimeException(done.cause());
                    }
                  });
                });
              }
            });

          } else {
            System.out.println("Tables are created");
          }

        });

      } else {
        System.out.println("Connection is failed");
      }
    });

  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    prepareDatabase();
  }

}
