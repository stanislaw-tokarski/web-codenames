package com.github.stanislawtokarski.codenames.service;

import com.github.stanislawtokarski.codenames.model.Color;
import com.github.stanislawtokarski.codenames.model.Player;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toUnmodifiableSet;

@Service
public class PlayersHolder {

    private final IconsDataHolder iconsDataHolder;
    private final EventLog eventLog;
    private final SessionsHolder sessionsHolder;

    private final Set<Player> players = ConcurrentHashMap.newKeySet();

    public PlayersHolder(IconsDataHolder iconsDataHolder, EventLog eventLog, SessionsHolder sessionsHolder) {
        this.iconsDataHolder = iconsDataHolder;
        this.eventLog = eventLog;
        this.sessionsHolder = sessionsHolder;
    }

    public void addPlayer(
            @Nullable UUID id,
            @Nonnull String name,
            @Nonnull Boolean isGod,
            @Nonnull Boolean isMaster,
            @Nullable Color team,
            @Nullable String icon,
            @Nonnull String sessionId) {
        if (id == null) {
            id = randomUUID();
            sessionsHolder.store(sessionId, id);
            logPlayerJoined(name, isGod);
        } else {
            players.remove(getById(id));
        }
        if (team == null) {
            team = assignColor();
        }
        if (icon == null) {
            icon = iconsDataHolder.assignNew();
        }
        var player = Player.builder()
                .withId(id)
                .withName(name)
                .withIsGod(isGod)
                .withIsMaster(isMaster)
                .withTeam(team)
                .withIconName(icon)
                .withSessionId(sessionId)
                .build();
        players.add(player);
    }

    public Player getById(UUID id) {
        return players.stream()
                .filter(byId(id))
                .findFirst()
                .orElse(null);
    }

    public Player getBySessionId(String sessionId) {
        var playerId = sessionsHolder.playerIdBySessionId(sessionId);
        return playerId != null ? getById(playerId) : null;
    }

    public Set<Player> getByTeam(Color team) {
        return players.stream()
                .filter(byColor(team))
                .collect(toUnmodifiableSet());
    }

    public boolean existsForSession(String sessionId) {
        return sessionsHolder.playerIdBySessionId(sessionId) != null;
    }

    public void remove(UUID playerId) {
        var player = getById(playerId);
        if (player != null) {
            var sessionId = sessionsHolder.sessionIdByPlayerId(playerId);
            sessionsHolder.destroy(sessionId);
            eventLog.addEvent(player.getName() + " removed from the game");
            players.remove(player);
        }
    }

    public void changeIcon(UUID playerId) {
        var player = getById(playerId);
        if (player != null) {
            iconsDataHolder.markAsNoLongerInUse(player.getIconName());
            addPlayer(
                    player.getId(), player.getName(), player.isGod(), player.isMaster(),
                    player.getTeam(), null, player.getSessionId());
        }
    }

    public void changeTeam(UUID playerId) {
        var player = getById(playerId);
        if (player != null) {
            var newColor = Color.reverse(player.getTeam());
            addPlayer(
                    player.getId(), player.getName(), player.isGod(), player.isMaster(),
                    newColor, player.getIconName(), player.getSessionId());
            eventLog.addEvent(format("%s moved to %s Team", player.getName(), newColor.getName()));
        }
    }

    public void changeType(UUID playerId) {
        var player = getById(playerId);
        if (player != null) {
            var isPromoteToMasterRequest = !player.isMaster();
            addPlayer(
                    player.getId(), player.getName(), player.isGod(), isPromoteToMasterRequest,
                    player.getTeam(), player.getIconName(), player.getSessionId());
            if (isPromoteToMasterRequest) {
                eventLog.addEvent(player.getName() + " promoted to Master");
            } else {
                eventLog.addEvent(player.getName() + " removed as Master");
            }
        }
    }

    private Color assignColor() {
        double totalCount = players.size();
        double redCount = players.stream()
                .filter(byColor(Color.RED))
                .count();
        return (totalCount / 2.0) > redCount ? Color.RED : Color.BLUE;
    }

    private void logPlayerJoined(String name, boolean isGod) {
        eventLog.addEvent(format("%s joined%s", name, isGod ? " as God!" : ""));
    }

    private Predicate<Player> byId(UUID id) {
        return player -> id.equals(player.getId());
    }

    private Predicate<Player> byColor(Color color) {
        return player -> color.equals(player.getTeam());
    }
}
