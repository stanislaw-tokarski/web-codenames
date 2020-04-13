package com.github.stanislawtokarski.codenames.service;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionsHolder {

    private final BiMap<String, UUID> sessions = Maps.synchronizedBiMap(HashBiMap.create());

    public void store(String sessionId, UUID playerId) {
        sessions.put(sessionId, playerId);
    }

    public void destroy(String sessionId) {
        sessions.remove(sessionId);
    }

    public UUID playerIdBySessionId(String sessionId) {
        return sessions.get(sessionId);
    }

    public String sessionIdByPlayerId(UUID playerId) {
        return sessions.inverse().get(playerId);
    }
}
