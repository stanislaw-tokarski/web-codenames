package com.github.stanislawtokarski.codenames.model;

import java.util.Objects;
import java.util.UUID;

public final class Player {

    private final UUID id;
    private final String sessionId;
    private final String name;
    private final boolean isGod;
    private final boolean isMaster;
    private final Color team;
    private final String iconName;

    private Player(Builder builder) {
        id = builder.getId();
        sessionId = builder.getSessionId();
        name = builder.getName();
        isGod = builder.isGod();
        isMaster = builder.isMaster();
        team = builder.getTeam();
        iconName = builder.getIconName();
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getName() {
        return name;
    }

    public boolean isGod() {
        return isGod;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public Color getTeam() {
        return team;
    }

    public String getIconName() {
        return iconName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return getId().equals(player.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public static final class Builder {

        private UUID id;
        private String sessionId;
        private String name;
        private boolean isGod;
        private boolean isMaster;
        private Color team;
        private String iconName;

        private Builder() {
        }

        public Builder withId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder withSessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withIsGod(boolean isGod) {
            this.isGod = isGod;
            return this;
        }

        public Builder withIsMaster(boolean isMaster) {
            this.isMaster = isMaster;
            return this;
        }

        public Builder withTeam(Color team) {
            this.team = team;
            return this;
        }

        public Builder withIconName(String icon) {
            iconName = icon;
            return this;
        }

        public Player build() {
            return new Player(this);
        }

        public UUID getId() {
            return id;
        }

        public String getSessionId() {
            return sessionId;
        }

        public String getName() {
            return name;
        }

        public boolean isGod() {
            return isGod;
        }

        public boolean isMaster() {
            return isMaster;
        }

        public Color getTeam() {
            return team;
        }

        public String getIconName() {
            return iconName;
        }
    }
}
