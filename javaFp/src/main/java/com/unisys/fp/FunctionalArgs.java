package com.unisys.fp;

@FunctionalInterface
interface Welcome {
    //no arg , no return
    void sayHello();
}

@FunctionalInterface
interface Server {
    void connect(String host, int port);
}
@FunctionalInterface
interface Adder{
    void add(int a,int b);
}

class Log {
    public void info(String info) {
        System.out.println(info);
    }
}
@FunctionalInterface
interface LoggerService {
    void log(Log log);
}

public class FunctionalArgs {
    public static void main(String[] args) {
        Welcome welcome = null;
        welcome = () -> {
            System.out.println("Hello");
        };
        welcome.sayHello();
        //
        Server server = null;
        server = (String host, int port) -> {
            System.out.println("Host :" + host + " " + "Port :" + port);
        };
        //call function and pass parameters
        server.connect("localhost", 8000);
        //reduce the type of variable :Type inference
        server = (host, port) -> {
            System.out.println("Host :" + host + " " + "Port :" + port);
        };
        //call function and pass parameters
        server.connect("localhost", 8000);

        Adder adder = (a,b)-> {
            System.out.println(a + b);
        };
        adder.add(10,20);
        //object
        LoggerService loggerService = (log)-> {
            log.info("logging...");
        };
        loggerService.log(new Log());

    }
}
