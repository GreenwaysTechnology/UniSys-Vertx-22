    package com.unisys.fp;

    //regular interface :having multiple abstract methods
    //interface Greeter {
    //    String sayGreet();
    //    String sayHello();
    //}
    //Functional Interface
    @FunctionalInterface
    interface Greeter {
        //SAM - single abstract method
        String sayGreet();
        //implementation - default methods
        default String sayHello() {
            return "Hello";
        }
        static String sayHai(){
            return "Hai";
        }
    }


    public class FunctionalInterfaceRules {
        public static void main(String[] args) {
            Greeter greeter = null;
            greeter = () -> {
                return "Hello";
            };
            System.out.println(greeter.sayGreet());
            System.out.println(greeter.sayHello());
            System.out.println(Greeter.sayHai());
        }
    }
