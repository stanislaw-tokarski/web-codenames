package com.github.stanislawtokarski.codenames.util;

import com.github.stanislawtokarski.codenames.model.CardType;
import com.vaadin.flow.component.html.Image;

import java.io.File;
import java.util.Set;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toUnmodifiableSet;

public class ImageLoader {

    private static final String RESOURCES_PATH = "src/main/resources/META-INF/resources/img/";
    private static final String PARROTS_DIR = "icons/parrots/";
    private static final String OTHERS_DIR = "icons/others/";
    private static final String IMG_CLASSIC = "img/classic/";
    private static final String IMG_TINY = "img/tiny/";
    private static final String RED_AGENT = "red_card.png";
    private static final String BLUE_AGENT = "blue_card.png";
    private static final String MURDERER = "black_card.png";
    private static final String INNOCENT = "innocent_card.png";

    private ImageLoader() {
    }

    public static Image getFor(CardType cardType, boolean tiny) {
        switch (cardType) {
            case RED_AGENT:
                return new Image(prefix(tiny) + RED_AGENT, "Red Agent");
            case BLUE_AGENT:
                return new Image(prefix(tiny) + BLUE_AGENT, "Blue Agent");
            case INNOCENT:
                return new Image(prefix(tiny) + INNOCENT, "Innocent");
            case MURDERER:
                return new Image(prefix(tiny) + MURDERER, "Murderer");
            default:
                throw new IllegalArgumentException("No image available for type: " + cardType);
        }
    }

    public static Set<String> allParrotsIconNames() {
        return allIconsFromDirectory(PARROTS_DIR);
    }

    public static Set<String> allOthersIconNames() {
        return allIconsFromDirectory(OTHERS_DIR);
    }

    private static Set<String> allIconsFromDirectory(String directory) {
        var fullPath = RESOURCES_PATH + directory;
        return stream(requireNonNull(new File(fullPath).listFiles()))
                .map(toIconName(directory))
                .collect(toUnmodifiableSet());
    }

    private static Function<File, String> toIconName(String directoryName) {
        return file -> format("img/%s%s", directoryName, file.getName());
    }

    private static String prefix(boolean tiny) {
        return tiny ? IMG_TINY : IMG_CLASSIC;
    }
}
