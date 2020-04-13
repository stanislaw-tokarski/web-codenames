package com.github.stanislawtokarski.codenames.model;

import java.util.concurrent.ThreadLocalRandom;

public enum Color {

    BLUE("Blue"),
    RED("Red");

    private final String name;

    Color(String name) {
        this.name = name;
    }

    public static Color random() {
        return ThreadLocalRandom.current().nextBoolean() ? BLUE : RED;
    }

    public static Color reverse(Color color) {
        return RED.equals(color) ? BLUE : RED;
    }

    public String getName() {
        return name;
    }
}
