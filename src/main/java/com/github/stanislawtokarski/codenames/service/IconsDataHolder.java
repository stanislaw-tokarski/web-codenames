package com.github.stanislawtokarski.codenames.service;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static com.github.stanislawtokarski.codenames.util.ImageLoader.allOthersIconNames;
import static com.github.stanislawtokarski.codenames.util.ImageLoader.allParrotsIconNames;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class IconsDataHolder {

    private static final Logger log = getLogger(IconsDataHolder.class);

    private final Map<String, Boolean> iconsWithUse = Collections.synchronizedMap(Maps.newLinkedHashMap());
    private final AtomicInteger currentIconCounter = new AtomicInteger(0);

    public String assignNew() {
        var icon = iconsWithUse.entrySet().stream()
                .filter(notInUse())
                .collect(toUnmodifiableList())
                .get(getThenSetCounter())
                .getKey();
        iconsWithUse.put(icon, true);
        log.info("Assigning icon: {}", icon);
        return icon;
    }

    public void markAsNoLongerInUse(String icon) {
        iconsWithUse.put(icon, false);
    }

    @PostConstruct
    private void initIconsMap() {
        putAll(allParrotsIconNames());
        putAll(allOthersIconNames());
    }

    private void putAll(Set<String> icons) {
        icons.stream()
                .peek(iconName -> log.info("Loaded icon: {}", iconName))
                .forEach(icon -> iconsWithUse.put(icon, false));
    }

    private Predicate<Entry<String, Boolean>> notInUse() {
        return nameWithUse -> !nameWithUse.getValue();
    }

    private int getThenSetCounter() {
        var freeIconsCount = iconsWithUse.entrySet().stream()
                .filter(notInUse())
                .count();
        var current = currentIconCounter.getAndIncrement();
        if (current < freeIconsCount) {
            return current;
        } else {
            currentIconCounter.set(0);
            return 0;
        }
    }
}
