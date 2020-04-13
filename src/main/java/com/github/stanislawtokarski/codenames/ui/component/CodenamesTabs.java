package com.github.stanislawtokarski.codenames.ui.component;

import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import java.util.Map;

public class CodenamesTabs extends Tabs {

    private final Tab gameTab = new Tab("Game");
    private final Tab godTab = new Tab("God");
    private final Tab registrationTab = new Tab("Registration");

    private final Map<Tab, String> routes = Map.of(
            gameTab, "",
            godTab, "god",
            registrationTab, "registration"
    );

    public CodenamesTabs(String route) {
        this.add(gameTab, godTab, registrationTab);
        this.addSelectedChangeListener(e -> this.getUI().ifPresent(
                ui -> ui.navigate(routes.get(e.getSelectedTab()))
        ));
        switch (route) {
            case "god":
                this.setSelectedTab(godTab);
                break;
            case "registration":
                this.setSelectedTab(registrationTab);
                break;
            case "":
                this.setSelectedTab(gameTab);
        }
    }
}
