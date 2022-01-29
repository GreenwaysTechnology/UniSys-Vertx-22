package com.unisys.fp.hof;


@FunctionalInterface
interface OrderProcessor {
    void processOrder(int id, double value);
}
//Function(methods) Which takes another function as parameter

//Success
@FunctionalInterface
interface Resolve {
    void resolve(String response);
}

//Failure
@FunctionalInterface
interface Reject {
    void reject(Throwable response);
}

class OrderService {
    //    void process(int id, double value) {
//        System.out.println("id " + id + " Value " + value);
//    }
    //Higher order Function
    void process(OrderProcessor orderProcessor) {
        //invoke the function
        orderProcessor.processOrder(1, 10.78);
    }
}

//passing multiple functions as parameter
class LoginService {
    //Higher order function
    public void auth(String userName, String password, Resolve success, Reject failure) {
        if (userName.equals("admin") && password.equals("admin")) {
            success.resolve("Login Success"); // return Type is String
        } else {
            failure.reject(new RuntimeException("Login Failed")); //  return Type is Throwable
        }
    }
//
//    public String auth_(String userName, String password, Resolve success, Reject failure) {
//        if (userName.equals("admin") && password.equals("admin")) {
//            return "Login success";
//        } else {
//           //  return Type is Throwable
//        }
//        return  throw new RuntimeException("Login Failed");
//    }
}


public class HigherOrderFunctions {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        //orderService.process(1, 90.89);
        //passing function as parameter with variables
        OrderProcessor orderProcessor = (id, value) -> {
            System.out.println("id " + id + " Value " + value);
        };
        orderService.process(orderProcessor);
        //inline pattern
        orderService.process((((id, value) -> {
            System.out.println("id " + id + " Value " + value);
        })));
        orderService.process(((id, value) -> System.out.println("id " + id + " Value " + value)));
        //Login Service
        LoginService loginService = new LoginService();
        loginService.auth("admin", "admin", response -> System.out.println(response), err -> System.out.println(err));
        loginService.auth("xxx", "yyy", response -> System.out.println(response), err -> System.out.println(err));

    }
}
