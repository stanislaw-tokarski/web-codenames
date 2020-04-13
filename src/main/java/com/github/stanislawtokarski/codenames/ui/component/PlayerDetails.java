package com.github.stanislawtokarski.codenames.ui.component;

import com.github.stanislawtokarski.codenames.model.Player;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import static java.lang.String.format;

public class PlayerDetails extends HorizontalLayout {

    private PlayerDetails(String name, Image icon) {
        setSpacing(true);
        setPadding(true);
        add(new H5(name), icon);
    }

    public static PlayerDetails ofPlayer(Player player) {
        var name = player.isMaster() ? format("[M] %s", player.getName()) : player.getName();
        return new PlayerDetails(name, new Image(player.getIconName(), "Icon"));
    }
}
