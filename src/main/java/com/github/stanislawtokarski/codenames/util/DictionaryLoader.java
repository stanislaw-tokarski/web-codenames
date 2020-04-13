package com.github.stanislawtokarski.codenames.util;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.Set;

import static java.nio.file.Files.lines;
import static java.nio.file.Paths.get;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.slf4j.LoggerFactory.getLogger;

public class DictionaryLoader {

    private static final Logger log = getLogger(DictionaryLoader.class);

    private DictionaryLoader() {
    }

    public static Set<String> defaultDictionary() {
        try {
            return lines(get("src/main/resources/dictionary.txt")).collect(toUnmodifiableSet());
        } catch (IOException ex) {
            log.error("Cannot get default dictionary", ex);
            return emptySet();
        }
    }
}
