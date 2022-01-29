package com.unisys.fp.collections;

import java.util.List;

public class Streams {
    public static void main(String[] args) {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6);
        list.forEach(i -> System.out.println(i));
        list.forEach(System.out::println);
        System.out.println("Filtered data");
//        list.stream().filter(e -> e > 3).forEach(System.out::println);
        list.stream().filter(Streams::isGreater).forEach(System.out::println);

    }
    private static boolean isGreater(Integer e) {
        return e > 3;
    }
}
