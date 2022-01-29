package com.unisys.fp.builtininterfaces;

import java.util.function.Consumer;
import java.util.function.*;

public class BuiltInInterfaces {
    public static void main(String[] args) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(Thread.currentThread().getName());
//            }
//        };
        Runnable runnable = () -> System.out.println(Thread.currentThread().getName());
        Thread thread = new Thread(runnable);
        thread.start();
        thread = new Thread(() -> System.out.println(Thread.currentThread().getName()));
        thread.start();
        //Supplier
        Supplier<String> supplier = null;
        supplier = () -> "Hello";
        System.out.println(supplier.get());
        Consumer<String> consumer = name -> System.out.println(name);
        consumer.accept("Subramanian");
        //return only int
        IntSupplier intSupplier = () -> 100;
        System.out.println(intSupplier.getAsInt());

        Predicate<Integer> predicate = number -> number > 10;
        System.out.println(predicate.test(100));
        System.out.println(predicate.test(1));

        Function<String, String> function = input -> input;
        System.out.println(function.apply("Hello"));

        //Bi -two
        BiPredicate<Integer, Integer> biPredicate = (a, b) -> a > b;
        System.out.println(biPredicate.test(10,20));

    }


}
