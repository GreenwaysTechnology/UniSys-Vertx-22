package com.unisys.fp.coderefactoring;

@FunctionalInterface
interface Greeter {
    void sayHello();
}

//parameters
@FunctionalInterface
interface SetValue {
    void setValue(int a);
}

//returnvalue
@FunctionalInterface
interface Add {
    int add(int a, int b);
}


public class LambdaCodeRefactoring {
    public static void main(String[] args) {
        Greeter greeter = null;
        greeter = () -> {
            System.out.println("Hello");
        };
        greeter.sayHello();
        //Rule if function body has only one line of code, we can remove {}
        greeter = () -> System.out.println("Hello");
        greeter.sayHello();
        SetValue value = null;
        value = (a) -> {
            System.out.println(a);
        };
        value.setValue(10);
        //Rule : if function takes only one arg, you can remove ()
        value = a -> System.out.println(a);
        value.setValue(100);
        //Return value
        Add adder = null;
        adder = (a, b) -> {
            return a + b;
        };
        System.out.println(adder.add(1, 2));
        //Rule : if function has only return statement we can remove {} and return statement
        adder = (a, b) -> a + b;
        System.out.println(adder.add(1, 2));

    }
}
