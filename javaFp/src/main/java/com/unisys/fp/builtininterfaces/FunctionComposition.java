package com.unisys.fp.builtininterfaces;

import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionComposition {
    public static void main(String[] args) {
        Predicate<String> startsWith = text -> text.startsWith("A");
        Predicate<String> endsWith = text -> text.endsWith("x");
        //create new function by combining two functions
        //Predicate<String> startsWithAndEndsWith = text -> startsWith.test(text) && endsWith.test(text);
        Predicate<String> startsWithAndEndsWith = startsWith.and(endsWith);
        boolean result = startsWithAndEndsWith.test("A hardworking person must relax");
        if (result) {
            System.out.println("Text Starts With A and Ends with e");
        } else {
            System.out.println("No Match found");
        }
        ///////////////////////////////////////////////////////////////////////////////////////////
        Function<Integer, Integer> multiplyByTwo = value -> value * 2;
        Function<Integer, Integer> addByThree = value -> value + 3;

        //combine two function create new Function
        //compose execute the methods right to left
        Function<Integer, Integer> addThenMultiply = multiplyByTwo.compose(addByThree);
        System.out.println(addThenMultiply.apply(10));

        //andThen execute the methods left to right
        Function<Integer, Integer> multiplyAndAdd = multiplyByTwo.andThen(addByThree);
        System.out.println(multiplyAndAdd.apply(10));


    }
}
