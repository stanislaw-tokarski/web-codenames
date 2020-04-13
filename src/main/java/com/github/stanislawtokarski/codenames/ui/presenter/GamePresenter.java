package com.github.stanislawtokarski.codenames.ui.presenter;

import com.github.stanislawtokarski.codenames.model.Color;
import com.github.stanislawtokarski.codenames.service.BoardHolder;
import com.github.stanislawtokarski.codenames.service.CountdownService;
import com.github.stanislawtokarski.codenames.service.EventLog;
import com.github.stanislawtokarski.codenames.service.PlayersHolder;
import com.github.stanislawtokarski.codenames.ui.view.GameView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

import static java.lang.Integer.parseInt;
import static java.lang.Math.min;
import static java.lang.String.format;

@Route("")
@PageTitle("Codenames")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class GamePresenter extends CanonicalPresenter<GameView> {

    private final BoardHolder boardHolder;
    private final PlayersHolder playersHolder;
    private final EventLog eventLog;
    private final CountdownService countdownService;

    public GamePresenter(@Value("${game.ui.poll.interval}") int pollInterval,
                         BoardHolder boardHolder, PlayersHolder playersHolder, EventLog eventLog, CountdownService countdownService) {
        this.boardHolder = boardHolder;
        this.playersHolder = playersHolder;
        this.eventLog = eventLog;
        this.countdownService = countdownService;
        UI.getCurrent().setPollInterval(pollInterval);
        UI.getCurrent().addPollListener(e -> refresh());
    }

    @Override
    protected void refresh() {
        setPlayers();
        setBoard();
        setCountdown();
        view.refreshEventLog(eventLog.getEvents());
        setRolesRestrictions();
    }

    @PostConstruct
    private void initViewListeners() {
        view.initListeners(
                this::onBoardButtonClicked,
                e -> onStartCountdownButtonClicked(),
                e -> onResetCountdownButtonClicked());
        refresh();
    }

    private void setPlayers() {
        view.setPlayers(playersHolder.getByTeam(Color.RED), Color.RED);
        view.setPlayers(playersHolder.getByTeam(Color.BLUE), Color.BLUE);
    }

    private void setBoard() {
        var board = boardHolder.getBoard();
        if (board != null) {
            view.updateBoard(board.getFields());
            view.setCardsLeft(board.getCardsLeft());
        }
    }

    private void setCountdown() {
        var countdownInProgress = countdownService.getInProgress();
        if (countdownInProgress) {
            view.setCurrentStopwatchProgress(0.0, 1.0, min(countdownService.getProgress(), 1.0));
            view.setCurrentStopwatchPeriod(countdownService.getPeriod());
        } else {
            view.setCurrentStopwatchProgress(0.0, 1.0, 0.0);
        }
        view.configureStopwatchButtons(countdownInProgress);
    }

    private void setRolesRestrictions() {
        var alreadyRegistered = playersHolder.existsForSession(getSessionId());
        var player = playersHolder.getBySessionId(getSessionId());
        var isMaster = player != null && player.isMaster();
        view.setRolesRestrictions(alreadyRegistered, isMaster);
    }

    private void onBoardButtonClicked(ClickEvent<Button> e) {
        var board = boardHolder.getBoard();
        if (board != null) {
            var id = parseInt(e.getSource().getId().get());
            var field = board.getFields().get(id);
            if (!field.isClicked()) {
                field.setClicked(true);
                eventLog.addEvent(format("%s clicked by %s. Card type is %s",
                        field.getWord(), playerName(), field.getType().getName()));
                view.disableButton(id);
            }
        }
        refresh();
    }

    private void onStartCountdownButtonClicked() {
        var timeLeft = view.getStopwatchConfiguredTime();
        eventLog.addEvent(format("Time to hurry up, %s set the stopwatch for %s seconds!",
                playerName(), timeLeft));
        countdownService.setPeriod(timeLeft);
        countdownService.startCountdown(timeLeft);
        refresh();
    }

    private void onResetCountdownButtonClicked() {
        eventLog.addEvent(playerName() + " wants to stop the countdown");
        countdownService.resetCountdown();
        refresh();
    }

    private String playerName() {
        return playersHolder.getBySessionId(getSessionId()).getName();
    }
}
