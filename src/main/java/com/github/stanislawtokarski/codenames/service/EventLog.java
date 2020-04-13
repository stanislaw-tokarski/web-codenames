package com.github.stanislawtokarski.codenames.service;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.reverse;
import static java.time.Instant.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toList;

@Component
public class EventLog {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern("HH:mm:ss");
    private static final ZoneId POLAND_ZONE = ZoneId.of("Poland");

    private final Map<Instant, String> events = Maps.newConcurrentMap();

    public void addEvent(String event) {
        events.put(now(), event);
    }

    public List<String> getEvents() {
        return reverse(events.entrySet().stream()
                .sorted(comparingByKey())
                .map(e -> DATE_TIME_FORMATTER.format(e.getKey().atZone(POLAND_ZONE)) + " " + e.getValue())
                .collect(toList()));
    }
}
