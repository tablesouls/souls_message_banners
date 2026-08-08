package net.tablesouls.souls_message_banners.data;

public enum DisplayMode {
    RADIUS("radius"),
    DIMENSION("dimension"),
    ALL_PLAYERS("all_players");

    private final String id;

    DisplayMode(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public static DisplayMode fromId(String id) {
        for (DisplayMode mode : values()) {
            if (mode.id.equals(id)) return mode;
        }
        throw new IllegalArgumentException("Unknown display_mode \"" + id + "\"");
    }
}