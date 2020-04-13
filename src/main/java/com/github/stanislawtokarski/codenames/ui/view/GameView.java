package com.github.stanislawtokarski.codenames.ui.view;

import com.github.stanislawtokarski.codenames.model.Board;
import com.github.stanislawtokarski.codenames.model.CardType;
import com.github.stanislawtokarski.codenames.model.Color;
import com.github.stanislawtokarski.codenames.model.Player;
import com.github.stanislawtokarski.codenames.ui.component.*;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.lang.String.join;

public class GameView extends VerticalLayout {

    private static final String NEW_LINE = "\n";

    private final H3 notRegisteredUserHeader = new H3("Seems like you haven't registered yet, " +
            "please visit the Registration tab first if you want to join");
    private final TeamDetails redTeamDetails = new TeamDetails(true);
    private final TeamDetails blueTeamDetails = new TeamDetails(true);
    private final BoardComponent boardPreview = new BoardComponent(true, true);
    private final BoardComponent board = new BoardComponent(false, false);
    private final TextArea eventLog = new TextArea("Event log:");
    private final CardsLeftDetails cardsLeftDetails = new CardsLeftDetails();
    private final StopwatchComponent stopwatch = new StopwatchComponent();

    public GameView() {
        redTeamDetails.setSummaryText("Red Team:");
        blueTeamDetails.setSummaryText("Blue Team:");
        eventLog.setWidth("300px");
        eventLog.setHeight("610px");
        eventLog.setReadOnly(true);
        var teamsAndBoardPreviewLayout = new HorizontalLayout(redTeamDetails, blueTeamDetails, boardPreview);
        var boardAndEventLogLayout = new HorizontalLayout(board, eventLog);
        var statsAntStopwatchLayout = new HorizontalLayout(cardsLeftDetails, stopwatch);
        add(new CodenamesTabs(""), notRegisteredUserHeader,
                teamsAndBoardPreviewLayout, boardAndEventLogLayout, statsAntStopwatchLayout);
    }

    public void initListeners(
            ComponentEventListener<ClickEvent<Button>> boardButtonClickedListener,
            ComponentEventListener<ClickEvent<Button>> startCountdownButtonClickedListener,
            ComponentEventListener<ClickEvent<Button>> resetCountdownButtonClickedListener) {
        board.initListeners(boardButtonClickedListener);
        stopwatch.initListeners(startCountdownButtonClickedListener, resetCountdownButtonClickedListener);
    }

    public void setPlayers(Collection<Player> players, Color color) {
        switch (color) {
            case RED:
                redTeamDetails.setPlayers(players, Color.RED, null);
                return;
            case BLUE:
                blueTeamDetails.setPlayers(players, Color.BLUE, null);
                return;
            default:
                throw new IllegalArgumentException("Cannot set players for color " + color.getName());
        }
    }

    public void updateBoard(List<Board.Field> fields) {
        board.updateBoard(fields);
        boardPreview.updateBoard(fields);
    }

    public void disableButton(int id) {
        board.disableButton(id);
    }

    public void refreshEventLog(List<String> events) {
        eventLog.setValue(join(NEW_LINE, events));
    }

    public void setCardsLeft(Map<CardType, Integer> cardsLeft) {
        cardsLeftDetails.setCardsLeft(cardsLeft);
    }

    public int getStopwatchConfiguredTime() {
        return stopwatch.getConfiguredTime();
    }

    public void setCurrentStopwatchPeriod(int period) {
        stopwatch.setCurrentPeriod(period);
    }

    public void setCurrentStopwatchProgress(double min, double max, double current) {
        stopwatch.setCurrentProgress(min, max, current);
    }

    public void configureStopwatchButtons(boolean inProgress) {
        stopwatch.configureButtons(inProgress);
    }

    public void setRolesRestrictions(boolean isRegistered, boolean isMaster) {
        notRegisteredUserHeader.setVisible(!isRegistered);
        boardPreview.setVisible(isRegistered && isMaster);
        board.setRolesRestrictions(isRegistered);
        stopwatch.setRolesRestrictions(isRegistered);
    }
}
