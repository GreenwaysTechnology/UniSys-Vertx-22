package com.unisys.fp;

@FunctionalInterface
interface Multipler {
    int multiply(int a, int b);
}


public class ReturnValues {
    public static void main(String[] args) {
        Multipler multipler = (a, b) -> {
            return a * b;
        };
        System.out.println(multipler.multiply(10,10));
    }
}
