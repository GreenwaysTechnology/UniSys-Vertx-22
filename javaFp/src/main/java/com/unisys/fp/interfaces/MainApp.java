package com.unisys.fp.interfaces;

//interfaces
interface Greeter {
    String sayGreet();
}

class HelloImpl implements Greeter {
    @Override
    public String sayGreet() {
        return "Hello";
    }
}
class HaiImpl implements Greeter {
    @Override
    public String sayGreet() {
        return "Hai!!!";
    }
}
public class MainApp {
    public static void main(String[] args) {
        //With impl
        Greeter greeter = null;
        //implementation
        greeter = new HelloImpl();
        System.out.println(greeter.sayGreet());
        //hai
        greeter = new HaiImpl();
        System.out.println(greeter.sayGreet());
        //Using inner class pattern -  anonymous inner class pattern
        greeter = new Greeter() {
            @Override
            public String sayGreet() {
                return "Welcome";
            }
        };
        System.out.println(greeter.sayGreet());

        greeter = new Greeter() {
            @Override
            public String sayGreet() {
                return "GreetMe";
            }
        };
        System.out.println(greeter.sayGreet());



    }
}
