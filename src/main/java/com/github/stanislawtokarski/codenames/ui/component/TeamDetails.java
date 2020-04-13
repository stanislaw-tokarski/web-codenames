package com.github.stanislawtokarski.codenames.ui.component;

import com.github.stanislawtokarski.codenames.model.Color;
import com.github.stanislawtokarski.codenames.model.Player;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.github.stanislawtokarski.codenames.ui.component.PlayerDetails.ofPlayer;
import static java.lang.String.format;
import static java.util.Comparator.comparing;

public class TeamDetails extends Details {

    private final boolean readOnly;

    private List<PlayerAndIconRow> playerAndIconRows = new ArrayList<>();

    public TeamDetails(boolean readOnly) {
        this.readOnly = readOnly;
        setOpened(true);
    }

    public void setPlayers(Collection<Player> players, Color color, PlayerAndIconRow.PlayerAndIconRowListeners playerAndIconRowListeners) {
        var sortedPlayers = players.stream()
                .sorted(comparing(Player::getName));
        Component[] content;
        if (readOnly) {
            content = sortedPlayers
                    .map(PlayerDetails::ofPlayer)
                    .toArray(Component[]::new);
        } else {
            content = sortedPlayers
                    .map(player -> new PlayerAndIconRow(player, playerAndIconRowListeners))
                    .peek(playerAndIconRows::add)
                    .toArray(Component[]::new);
        }
        setContent(null);
        if (content.length == 0) {
            addContent(new Text("Nobody here yet!"));
        } else {
            addContent(content);
        }
        setSummaryText(format("%s Team:", color.getName()));
    }

    public void setRolesRestrictions(boolean isRegistered, boolean isGod) {
        playerAndIconRows.forEach(playerAndIconRow -> playerAndIconRow.setRolesRestrictions(isRegistered, isGod));
    }

    public static final class PlayerAndIconRow extends VerticalLayout {

        private final Button changePlayerTypeButton = new Button();
        private final Button changeIconButton = new Button("Change icon");
        private final Button changeTeamButton = new Button("Change team");
        private final Button deleteButton = new Button("Delete player");

        public PlayerAndIconRow(Player player, PlayerAndIconRowListeners playerAndIconRowListeners) {
            initListeners(playerAndIconRowListeners);
            setIdForButtons(player);
            setHeightForButtons();
            changePlayerTypeButton.setText(player.isMaster() ? "Remove as Master" : "Promote to Master");
            var upperLayout = new HorizontalLayout(changeIconButton, changePlayerTypeButton);
            var bottomLayout = new HorizontalLayout(changeTeamButton, deleteButton);
            var buttonsLayout = new VerticalLayout(upperLayout, bottomLayout);
            var playerDetails = ofPlayer(player);
            add(playerDetails, buttonsLayout);
        }

        private void initListeners(PlayerAndIconRowListeners playerAndIconRowListeners) {
            changeIconButton.addClickListener(playerAndIconRowListeners.changeIconButtonClickedListener);
            changePlayerTypeButton.addClickListener(playerAndIconRowListeners.changePlayerTypeButtonClickedListener);
            changeTeamButton.addClickListener(playerAndIconRowListeners.changeTeamButtonClickedListener);
            deleteButton.addClickListener(playerAndIconRowListeners.deletePlayerButtonClickedListener);
        }

        private void setRolesRestrictions(boolean isRegistered, boolean isGod) {
            changeIconButton.setEnabled(isRegistered);
            changeTeamButton.setEnabled(isRegistered);
            changePlayerTypeButton.setEnabled(isGod);
            deleteButton.setEnabled(isGod);
        }

        private void setHeightForButtons() {
            Stream.of(changePlayerTypeButton, changeIconButton, changeTeamButton, deleteButton)
                    .forEach(button -> button.setHeight("25px"));
        }

        private void setIdForButtons(Player player) {
            changePlayerTypeButton.setId(id(player));
            changeIconButton.setId(id(player));
            changeTeamButton.setId(id(player));
            deleteButton.setId(id(player));
        }

        private String id(Player player) {
            return player.getId().toString();
        }

        public static final class PlayerAndIconRowListeners {

            private final ComponentEventListener<ClickEvent<Button>> changeIconButtonClickedListener;
            private final ComponentEventListener<ClickEvent<Button>> changePlayerTypeButtonClickedListener;
            private final ComponentEventListener<ClickEvent<Button>> changeTeamButtonClickedListener;
            private final ComponentEventListener<ClickEvent<Button>> deletePlayerButtonClickedListener;

            public PlayerAndIconRowListeners(
                    ComponentEventListener<ClickEvent<Button>> changeIconButtonClickedListener,
                    ComponentEventListener<ClickEvent<Button>> changePlayerTypeButtonClickedListener,
                    ComponentEventListener<ClickEvent<Button>> changeTeamButtonClickedListener,
                    ComponentEventListener<ClickEvent<Button>> deletePlayerButtonClickedListener) {
                this.changeIconButtonClickedListener = changeIconButtonClickedListener;
                this.changePlayerTypeButtonClickedListener = changePlayerTypeButtonClickedListener;
                this.changeTeamButtonClickedListener = changeTeamButtonClickedListener;
                this.deletePlayerButtonClickedListener = deletePlayerButtonClickedListener;
            }
        }
    }
}
