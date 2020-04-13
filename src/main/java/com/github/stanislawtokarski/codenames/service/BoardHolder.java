package com.github.stanislawtokarski.codenames.service;

import com.github.stanislawtokarski.codenames.model.Board;
import com.github.stanislawtokarski.codenames.model.CardType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.stanislawtokarski.codenames.model.Board.fromWords;
import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toUnmodifiableSet;

@Component
public class BoardHolder {

    private final EventLog eventLog;
    private final AtomicReference<Board> board = new AtomicReference<>();
    private final Set<String> dictionary = ConcurrentHashMap.newKeySet();

    public BoardHolder(EventLog eventLog) {
        this.eventLog = eventLog;
    }

    public Board getBoard() {
        return board.get();
    }

    public boolean newBoard() {
        if (dictionary.size() >= 25) {
            var copy = new ArrayList<>(dictionary);
            shuffle(copy);
            var selected = copy.stream()
                    .limit(25)
                    .collect(toUnmodifiableSet());
            dictionary.removeAll(selected);
            board.set(fromWords(selected));
            logBoardCreated();
            return true;
        }
        return false;
    }

    public void addWords(Collection<String> words) {
        dictionary.addAll(
                words.stream()
                        .map(String::toUpperCase)
                        .collect(toUnmodifiableSet()));
    }

    public Set<String> getWords() {
        return dictionary;
    }

    public void clearWords() {
        dictionary.clear();
    }

    private void logBoardCreated() {
        eventLog.addEvent("New board created. Words left: " + dictionary.size());
        var cards = board.get().getCardsLeft();
        var whoStarts = cards.get(CardType.BLUE_AGENT) > cards.get(CardType.RED_AGENT) ? "Blue Team" : "Red Team";
        eventLog.addEvent(whoStarts + " starts!");
    }
}
