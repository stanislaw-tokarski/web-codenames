package com.github.stanislawtokarski.codenames.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toMap;

public class Board {

    private final List<Field> fields;

    private Board(List<Field> fields) {
        this.fields = fields;
    }

    public static Board fromWords(Set<String> words) {
        assert words.size() == 25;
        var wordsIterator = words.iterator();
        var fields = new ArrayList<Field>(25);
        var advantageCard = ThreadLocalRandom.current().nextBoolean() ? CardType.RED_AGENT : CardType.BLUE_AGENT;
        fields.add(new Field(wordsIterator.next(), advantageCard));
        stream(CardType.values()).forEach(type ->
                IntStream.range(0, type.getCount()).boxed()
                        .forEach(i -> fields.add(new Field(wordsIterator.next(), type))));
        shuffle(fields);
        return new Board(fields);
    }

    public List<Field> getFields() {
        return fields;
    }

    public Map<CardType, Integer> getCardsLeft() {
        return fields.stream()
                .collect(toMap(Field::getType, f -> f.isClicked() ? 0 : 1, Integer::sum));
    }

    public static final class Field {

        private final String word;
        private final CardType type;
        private boolean clicked = false;

        private Field(String word, CardType type) {
            this.word = word;
            this.type = type;
        }

        public String getWord() {
            return word;
        }

        public CardType getType() {
            return type;
        }

        public boolean isClicked() {
            return clicked;
        }

        public void setClicked(boolean clicked) {
            this.clicked = clicked;
        }
    }
}
