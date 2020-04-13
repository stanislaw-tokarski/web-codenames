package com.github.stanislawtokarski.codenames.model;

public enum CardType {

    RED_AGENT(8, "Red Agent"),
    BLUE_AGENT(8, "Blue Agent"),
    MURDERER(1, "Murderer"),
    INNOCENT(7, "Innocent");

    private final int count;
    private final String name;

    CardType(int count, String name) {
        this.count = count;
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }
}
