package com.unisys.fp.functionalinterfaces;

//interface
interface Greeter {
    String sayGreet();
}

public class FunctionalMain {
    public static void main(String[] args) {
        Greeter greeter = null;
        //using old anonmous inner class Syntax
        greeter = new Greeter() {
            @Override
            public String sayGreet() {
                return "Hai";
            }
        };
        System.out.println(greeter.sayGreet());
        //using Functional Style : Lambda Style
        greeter = () -> {
            return "Hai Lambda";
        };
        System.out.println(greeter.sayGreet());

    }
}
