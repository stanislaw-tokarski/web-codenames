package com.github.stanislawtokarski.codenames.ui.view;

import com.github.stanislawtokarski.codenames.model.Color;
import com.github.stanislawtokarski.codenames.model.Player;
import com.github.stanislawtokarski.codenames.ui.component.CodenamesTabs;
import com.github.stanislawtokarski.codenames.ui.component.TeamDetails;
import com.github.stanislawtokarski.codenames.ui.component.TeamDetails.PlayerAndIconRow.PlayerAndIconRowListeners;
import com.github.stanislawtokarski.codenames.ui.component.WordsComponent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Collection;

import static com.vaadin.flow.component.notification.Notification.Position.MIDDLE;
import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_ERROR;

public class GodView extends VerticalLayout {

    private final H3 notRegisteredUserHeader = new H3("Seems like you haven't registered yet, " +
            "please visit the Registration tab first if you want to join");
    private final Button newGameButton = new Button("PREPARE NEW GAME");
    private final WordsComponent wordsComponent = new WordsComponent();
    private final TeamDetails redTeamDetails = new TeamDetails(false);
    private final TeamDetails blueTeamDetails = new TeamDetails(false);

    private PlayerAndIconRowListeners teamDetailsRowListener;

    public GodView() {
        redTeamDetails.setSummaryText("Red Team:");
        blueTeamDetails.setSummaryText("Blue Team:");
        var playersLayout = new HorizontalLayout(redTeamDetails, blueTeamDetails);
        var configLayout = new HorizontalLayout(wordsComponent, playersLayout);
        add(new CodenamesTabs("god"), notRegisteredUserHeader, newGameButton, configLayout);
    }

    public void initListeners(
            ComponentEventListener<ClickEvent<Button>> newButtonButtonClickedListener,
            ComponentEventListener<ClickEvent<Button>> submitWordsButtonClickedListener,
            ComponentEventListener<ClickEvent<Button>> clearWordsButtonClickedListener,
            ComponentEventListener<ClickEvent<Button>> defaultWordsButtonClickedListener,
            ComponentEventListener<ClickEvent<Button>> changeIconButtonClickedListener,
            ComponentEventListener<ClickEvent<Button>> changePlayerTypeButtonClickedListener,
            ComponentEventListener<ClickEvent<Button>> deletePlayerButtonClickedListener,
            ComponentEventListener<ClickEvent<Button>> changeTeamButtonClickedListener) {
        newGameButton.addClickListener(newButtonButtonClickedListener);
        wordsComponent.initListeners(
                submitWordsButtonClickedListener, clearWordsButtonClickedListener, defaultWordsButtonClickedListener);
        teamDetailsRowListener = new PlayerAndIconRowListeners(
                changeIconButtonClickedListener, changePlayerTypeButtonClickedListener,
                changeTeamButtonClickedListener, deletePlayerButtonClickedListener);
    }

    public String getWords() {
        return wordsComponent.getWords();
    }

    public void setWords(String words) {
        wordsComponent.setWords(words);
    }

    public void clearWords() {
        wordsComponent.clearWords();
    }

    public void setWordsLeft(int wordsLeftCount) {
        wordsComponent.setWordsLeft(wordsLeftCount);
    }

    public void setPlayers(Collection<Player> players, Color color) {
        switch (color) {
            case RED:
                redTeamDetails.setPlayers(players, Color.RED, teamDetailsRowListener);
                return;
            case BLUE:
                blueTeamDetails.setPlayers(players, Color.BLUE, teamDetailsRowListener);
                return;
            default:
                throw new IllegalArgumentException("Cannot set players for color " + color.getName());
        }
    }

    public void showErrorNotification(String message) {
        Notification.show(message, 5000, MIDDLE).addThemeVariants(LUMO_ERROR);
    }

    public void setRolesRestrictions(boolean isRegistered, boolean isGod) {
        notRegisteredUserHeader.setVisible(!isRegistered);
        newGameButton.setEnabled(isRegistered && isGod);
        wordsComponent.setRolesRestrictions(isRegistered, isGod);
        redTeamDetails.setRolesRestrictions(isRegistered, isGod);
        blueTeamDetails.setRolesRestrictions(isRegistered, isGod);
    }
}
