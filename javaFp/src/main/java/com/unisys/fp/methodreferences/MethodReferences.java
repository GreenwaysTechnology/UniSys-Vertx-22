package com.unisys.fp.methodreferences;

import java.util.function.BiFunction;
import java.util.function.Supplier;

//
@FunctionalInterface
interface InventoryProcessor {
    String process(boolean isStockAvailable);
}

@FunctionalInterface
interface Greeter {
    Hello sayHello(String message);
}

class Hello {
    private String message;

    public Hello() {
    }

    public Hello(String message) {
        this.message = message;
        System.out.println(this.message);
    }

    public String getMessage() {
        return message;
    }
}

class OrderService {
    public boolean isStockAvailable() {
        return true;
    }
}

class InventoryService {
    //Higher Order function
    public void updateInventor(InventoryProcessor processor) {
        System.out.println(processor.process(true));
    }
}

class Calculator {
    public static int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }
}

class MicroTask {
    //static method
    public static void processV2() {
        System.out.println(Thread.currentThread().getName());
    }

    //instance method
    public void process() {
        System.out.println(Thread.currentThread().getName());
    }
}

class Task {

    //thread logic
    private void process() {
        //thread logic goes here
        System.out.println(Thread.currentThread().getName());
    }

    void startTask() {
        Thread thread = null;
        //Thread using Anonymous pattern
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        });
        thread.start();
        //Lambda syntax
        thread = new Thread(() -> System.out.println(Thread.currentThread().getName()));
        thread.start();
        //Runnable logic into another method : using lambda
        thread = new Thread(() -> this.process());
        thread.start();
        //Runnable logic into separate class with instance method: using lambda
        MicroTask microTask = new MicroTask();
        thread = new Thread(() -> microTask.process());
        thread.start();
        //Runnable logic into separate class with static method: using lambda
        thread = new Thread(() -> MicroTask.processV2());
        thread.start();
        //Runnable logic into another method : using method Reference
        thread = new Thread(this::process);
        thread.start();
        //Runnable logic into separate class with instance method: using MethodReference
        thread = new Thread(microTask::process);
        thread.start();
        //Runnable logic into separate class with static method: using Method Reference
        thread = new Thread(MicroTask::processV2);
        thread.start();

    }
}

public class MethodReferences {

    private static String process(boolean res) {
        return "Inventory processed" + res;
    }

    public static void main(String[] args) {
        Task task = new Task();
        //task.startTask();
        //
        OrderService orderService = new OrderService();
        //Buit in Supplier interface
        Supplier<Boolean> orderSupplier = null;
        //Supplier with lambda
        orderSupplier = () -> orderService.isStockAvailable();
        System.out.println(orderSupplier.get());
        //Supplier with Method Reference
        orderSupplier = orderService::isStockAvailable;
        System.out.println(orderSupplier.get());
        InventoryService inventoryService = new InventoryService();
        //hof with lambda
//        inventoryService.updateInventor(res -> {
//            return "Inventory processed" + res;
//        });
        inventoryService.updateInventor(res -> "Inventory processed" + res);
        //hof with Hof
        inventoryService.updateInventor(MethodReferences::process);

        //
        //BiFunction<Integer, Integer, Integer> adder = (a, b) -> Calculator.add(a, b);
        BiFunction<Integer, Integer, Integer> adder = Calculator::add;
        System.out.println(adder.apply(10, 10));

        Calculator calculator = new Calculator();
        //lambda with input and output
        BiFunction<Integer, Integer, Integer> subtracter = (a, b) -> calculator.subtract(a, b);
        System.out.println(subtracter.apply(10, 2));
        BiFunction<Integer, Integer, Integer> subtracterMethodRef = calculator::subtract;
        System.out.println(subtracterMethodRef.apply(10, 2));

        //object creation using :: method reference
//        Greeter greeter = message -> new Hello(message);
        Greeter greeter = Hello::new;
        System.out.println(greeter.sayHello("Hello"));

    }
}
