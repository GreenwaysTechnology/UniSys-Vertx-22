package com.unisys.callback;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.example.util.Runner;

//model
class User {
  private String userName;
  private String password;

  public User(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}

class PageVerticle extends AbstractVerticle {

  //chanining

  //api - getUser - return user Details
  //login - which takes input from the getUser
  private Future<String> getUser() {
    System.out.println("Get User is called");
    User user = new User("admin", "admin");
//    User user = null;
    if (user != null) {
      return Future.succeededFuture(user.getUserName());
    } else {
      return Future.failedFuture("User not Found");
    }
  }

  //login method
  private Future<String> login(String userName) {
    System.out.println("login is called");

    if (userName.equals("admin")) {
      return Future.succeededFuture("Login Success");
    } else {
      return Future.failedFuture("Login failed");
    }
  }

  //page
  private Future<String> showPage(String status) {
    System.out.println("ShowPage is called");

    if (status.equals("Login Success")) {
      return Future.succeededFuture("Welcome to Dashboard");
    } else {
      return Future.failedFuture("Please Retry!");
    }

  }

  @Override

  public void start() throws Exception {
    super.start();
//    getUser().onComplete(e -> {
//      if (e.succeeded()) {
//        System.out.println(e.result());
//        //call login method
//        login(e.result()).onComplete(loginEvent -> {
//          if (loginEvent.succeeded()) {
//            System.out.println(loginEvent.result());
//            showPage(loginEvent.result()).onComplete(pageEvent -> {
//              if (pageEvent.succeeded()) {
//                System.out.println(pageEvent.result());
//              } else {
//                System.out.println(pageEvent.cause());
//              }
//            });
//          } else {
//            System.out.println(loginEvent.cause());
//          }
//        });
//      } else {
//        System.out.println(e.cause());
//      }
//    });
//    //How to refactor this above code
//    getUser().onSuccess(user -> {
//      System.out.println(user);
//      login(user).onSuccess(status -> {
//        System.out.println(status);
//        showPage(status).onSuccess(page -> {
//          System.out.println(page);
//        }).onFailure(err -> {
//          System.out.println(err);
//        });
//      }).onFailure(err -> {
//        System.out.println(err);
//      });
//    }).onFailure(err -> {
//      System.out.println(err);
//    });

    //compose method
    System.out.println("Using compose Method");
//    getUser().compose(user -> {
//        System.out.println("Get user is called");
//        return login(user); //Return Future
//      }).compose(status -> {
//        System.out.println("Login is called");
//        return showPage(status);
//      })
//      .onSuccess(s -> {
//        System.out.println("Show page is called");
//        System.out.println(s);
//      }).onFailure(err -> {
//        System.out.println(err);
//      });
    // "hello".trim().toUpperCase()
    //Future.compose().compose().onSuccess().OnFailure
    getUser()
      .compose(user -> login(user))
      .compose(status -> showPage(status))
      .onSuccess(s -> {
        System.out.println(s);
      }).onFailure(err -> {
        System.out.println(err);
      });
    getUser()
      .compose(this::login)
      .compose(this::showPage)
      .onSuccess(System.out::println)
      .onFailure(System.out::println);
  }
}


public class CallbackVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(CallbackVerticle.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    vertx.deployVerticle(new PageVerticle());
  }
}
