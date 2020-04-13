package com.github.stanislawtokarski.codenames.ui.presenter;

import com.github.stanislawtokarski.codenames.model.Color;
import com.github.stanislawtokarski.codenames.service.BoardHolder;
import com.github.stanislawtokarski.codenames.service.PlayersHolder;
import com.github.stanislawtokarski.codenames.ui.view.GodView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.UUID;

import static com.github.stanislawtokarski.codenames.util.DictionaryLoader.defaultDictionary;
import static java.lang.String.join;
import static java.util.Arrays.stream;
import static java.util.UUID.fromString;
import static java.util.stream.Collectors.toUnmodifiableSet;

@Route("god")
@PageTitle("Codenames - GOD")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class GodPresenter extends CanonicalPresenter<GodView> {

    private static final String NEW_LINE = "\n";

    private final BoardHolder boardHolder;
    private final PlayersHolder playersHolder;

    public GodPresenter(@Value("${god.ui.poll.interval}") int pollInterval,
                        BoardHolder boardHolder, PlayersHolder playersHolder) {
        this.boardHolder = boardHolder;
        this.playersHolder = playersHolder;
        UI.getCurrent().setPollInterval(pollInterval);
        UI.getCurrent().addPollListener(e -> refresh());
    }

    @Override
    protected void refresh() {
        setPlayers();
        setWords();
        setRolesRestrictions();
    }

    @PostConstruct
    private void initViewListeners() {
        view.initListeners(
                e -> onNewGameButtonClicked(),
                e -> onSubmitWordsButtonClicked(),
                e -> onClearWordsButtonClicked(),
                e -> onDefaultWordsButtonClicked(),
                this::onChangeIconButtonClicked,
                this::onChangePlayerTypeButtonClicked,
                this::onDeletePlayerButtonClicked,
                this::onChangeTeamButtonClicked);
        refresh();
    }

    private void setPlayers() {
        view.setPlayers(playersHolder.getByTeam(Color.RED), Color.RED);
        view.setPlayers(playersHolder.getByTeam(Color.BLUE), Color.BLUE);
    }

    private void setWords() {
        var words = boardHolder.getWords();
        view.setWords(join(NEW_LINE, words));
        view.setWordsLeft(words.size());
    }

    private void setRolesRestrictions() {
        var alreadyRegistered = playersHolder.existsForSession(getSessionId());
        var player = playersHolder.getBySessionId(getSessionId());
        var isGod = player != null && player.isGod();
        view.setRolesRestrictions(alreadyRegistered, isGod);
    }

    private void onNewGameButtonClicked() {
        var created = boardHolder.newBoard();
        if (created) {
            refresh();
        } else {
            view.showErrorNotification("Not enough words to prepare new board!");
        }
    }

    private void onSubmitWordsButtonClicked() {
        var parse = stream(view.getWords().split(NEW_LINE))
                .filter(StringUtils::isNotBlank)
                .collect(toUnmodifiableSet());
        boardHolder.addWords(parse);
        refresh();
    }

    private void onClearWordsButtonClicked() {
        boardHolder.clearWords();
        view.clearWords();
        refresh();
    }

    private void onDefaultWordsButtonClicked() {
        boardHolder.addWords(defaultDictionary());
        refresh();
    }

    private void onChangeIconButtonClicked(ClickEvent<Button> e) {
        playersHolder.changeIcon(playerId(e));
        refresh();
    }

    private void onChangePlayerTypeButtonClicked(ClickEvent<Button> e) {
        playersHolder.changeType(playerId(e));
        refresh();
    }

    private void onDeletePlayerButtonClicked(ClickEvent<Button> e) {
        playersHolder.remove(playerId(e));
        refresh();
    }

    private void onChangeTeamButtonClicked(ClickEvent<Button> e) {
        playersHolder.changeTeam(playerId(e));
        refresh();
    }

    private UUID playerId(ClickEvent<Button> e) {
        return fromString(e.getSource().getId().get());
    }
}
